package com.search.elasticsearch.automatic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.search.common.domain.BusinessException;
import com.search.common.domain.BusinessResponseEnum;
import com.search.common.domain.PageData;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * elastic service基类
 * <p>
 * 基础描述：
 * 索引的前缀名称由模板T代表的class中，对class进行的@ElasticDocument注解来指定
 * field的mapping，由T类中对各个field进行注解来指定
 * getIndexName为索引前缀
 * getWriteIndexAlias为当前写索引的别名，格式为indexName_w
 * 索引格式为indexName-YYYY.MM.dd-num
 * <p>
 * 工作流程：
 * 1、每隔10秒进行一次模板和索引检测
 * 如果模板不存在，就添加模板
 * 如果模板存在，就比较模板是否发生了变化（shard,relica,max_result_window,mappings），发生变化就重新创建模板
 * 如果索引不存在，则创建第一个索引，并建立write alias
 * 如果索引存在，就比较模板是否发生了变化，如果变化了，就无条件rollover一个新的index作为写入索引
 * TODO 注意，如果原有field的mappings发生了改变或者删除，需要手动调用remapping函数进行zero downtime的remapping（创建新的索引，把旧索引中的docs导入到新的索引中）
 * 2、每个60秒执行一次rollover，检查是否需要创建新的索引
 * <p>
 * //TODO remapping函数
 * <p>
 * Author：linyue
 * Date：2020/4/10
 */
@Slf4j
public abstract class ElasticSearchService<T extends Doc> {
    private static int SCROLL_SEARCH_WINDOW = 10000;

    private static int SCROLL_SEARCH_TIMEOUT_SECONDS = 3 * 60;

    private static String GROUP_NAME_CREATE_INDEX = "GROUP_NAME_CREATE_INDEX";

    private static String GROUP_NAME_ROLLOVER = "GROUP_NAME_ROLLOVER";

    private static String GROUP_NAME_PERIOD_INSERT = "GROUP_NAME_PERIOD_INSERT";

    private Class<T> docClass;

    private String indexName;


    private List<T> periodInsertList;


    //TODO 通过新的模板创建新的索引，把write alias指向新的索引
    //TODO 旧的索引都导一遍doc的话，时间太久了，考虑一下怎么设计
    public void remapping() {

    }

    /**
     * 1、传入的doc如果设置了id，就不会随机id
     * 2、函数执行完毕以后doc的id会被设置为es中的真实值
     *
     * @param doc
     */
    public void insert(T doc) {
        try {
            if (!indexExists()) {
                log.error("insert doc to es failed. index write alias not exists. index={}, doc={}", getIndexName(), doc);
                throw new IndexNotFoundException(getWriteIndexAlias());
            }
            doc.setGmtCreate(OffsetDateTime.now());
            doc.setGmtModified(OffsetDateTime.now());
            IndexRequest indexRequest = new IndexRequest(getWriteIndexAlias());
            indexRequest.source(JSONObject.toJSONString(doc), XContentType.JSON);
            if (doc.id != null) {
                indexRequest.id(doc.id);
                indexRequest.opType(DocWriteRequest.OpType.CREATE);
            }
            log.debug("insert doc. index={}, req={}", getIndexName(), indexRequest);
            IndexResponse response = getEsClient().index(indexRequest, RequestOptions.DEFAULT);
            doc.id = response.getId();
        } catch (IOException e) {
            log.error("insert doc to es failed. index={}, doc={}, err={}", getIndexName(), doc, e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void periodInsert(T doc) {
        synchronized (periodInsertList) {
            periodInsertList.add(doc);
            if (periodInsertMaxCount() > 0 && periodInsertList.size() >= periodInsertMaxCount()) {
                rawPeriodInsert("reach max count");
            }
        }
    }

    protected void rawPeriodInsert(String situationDesc) {
        synchronized (periodInsertList) {
            if (periodInsertList.isEmpty()) {
                log.debug("period insert docs but empty");
            } else {
                long before = System.nanoTime();
                bulkInsert(periodInsertList);
                long after = System.nanoTime();
                long cost = (after - before) / 1000000;
                log.info("period insert docs ({}}). count={}, period={}ms, maxCount={}, cost={}ms", situationDesc, periodInsertList.size(), periodInsertMillionSeconds(), periodInsertMaxCount(), cost);
                periodInsertList.clear();
            }
        }
    }

    /**
     * 1、传入的doc如果设置了id，就不会随机id
     * 2、函数执行完毕以后doc的id会被设置为es中的真实值
     *
     * @param docs
     */
    public void bulkInsert(List<T> docs) {
        if (docs == null || docs.isEmpty()) {
            return;
        }
        try {
            if (!indexExists()) {
                log.error("bulk insert doc to es failed. index write alias not exists. index={}, size={}", getIndexName(), docs.size());
                throw new IndexNotFoundException(getWriteIndexAlias());
            }
        } catch (IOException e) {
            log.error("bulk insert doc to es failed. check index exists fail. index={}, docs={}, err={}", getIndexName(), docs.size(), e);
            e.printStackTrace();
        }
        BulkRequest bulkRequest = new BulkRequest(getWriteIndexAlias());
        for (Doc doc : docs) {
            doc.setGmtCreate(OffsetDateTime.now());
            doc.setGmtModified(OffsetDateTime.now());
            IndexRequest indexRequest = new IndexRequest(getWriteIndexAlias());
            indexRequest.source(JSONObject.toJSONString(doc), XContentType.JSON);
            if (doc.id != null) {
                indexRequest.id(doc.id);
                indexRequest.opType(DocWriteRequest.OpType.CREATE);
            }
            bulkRequest.add(indexRequest);
        }
        List<String> ids = new ArrayList<>();
        docs.forEach(doc -> {
            ids.add(doc.id);
        });
        log.info("bulk insert doc. index={}, ids={}", getIndexName(), ids);
        log.info("bulk insert doc. index={}, req={}", getIndexName(), bulkRequest);
        try {
            BulkResponse resp = getEsClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            if (resp.hasFailures()) {
                log.error("bulk insert doc fail. fail_message={}", resp.buildFailureMessage());
            }
            BulkItemResponse[] items = resp.getItems();
            Iterator<T> iter = docs.iterator();
            for (BulkItemResponse item : items) {
                T doc = iter.next();
                doc.id = item.getId();
                log.info("bulk insert doc response. index={}, id={}", getIndexName(), item.getId());
            }
        } catch (Exception e) {
            log.error("bulk insert doc to es failed. index={}, docs={}, err={}", getIndexName(), docs.size(), e);
            throw new RuntimeException(e);
        }
    }

    public PageData<T> paginationSearch(BoolQueryBuilder boolQueryBuilder, String sortField, SortOrder order, int pageSize, int page) {
        return paginationSearch(boolQueryBuilder, sortField, order, pageSize, page, null, null, null);
    }

    public PageData<T> paginationSearch(BoolQueryBuilder boolQueryBuilder, String sortField, SortOrder order, int pageSize, int page, String timeField, OffsetDateTime timeFrom, OffsetDateTime timeTo) {
        PageData<T> result = filterPage(pageSize, page);
        pageSize = result.pageSize;
        page = result.pageNum;
        long count = count(boolQueryBuilder, timeField, timeFrom, timeTo, true);
        result.list = search(boolQueryBuilder, sortField, order, pageSize, page, timeField, timeFrom, timeTo, true);
        result.total = count;
        return result;
    }

    public PageData<DocCount<T>> paginationGroupBy(BoolQueryBuilder boolQueryBuilder, String groupField
            , String sortField, boolean orderByCount, SortOrder order, int pageSize, int page
            , String timeField, OffsetDateTime timeFrom, OffsetDateTime timeTo, boolean reduceIndicesScope) {
        PageData<DocCount<T>> result = new PageData<>();
        result.list = new ArrayList<>();
        String[] indices = addTimeQuery(boolQueryBuilder, timeField, timeFrom, timeTo);
        if (!reduceIndicesScope) {
            indices = new String[]{getIndexPattern()};
        }
        if (sortField == null) {
            sortField = "gmtCreate";
        }
        if (order == null) {
            order = SortOrder.DESC;
        }
        TopHitsAggregationBuilder topHits = AggregationBuilders.topHits("top_hits").size(1)
                .sort(sortField, order).fetchSource(new String[]{"_id", sortField}, null);
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("term_agg").field(groupField)
                .size(Integer.MAX_VALUE).collectMode(Aggregator.SubAggCollectionMode.BREADTH_FIRST)
                .order(BucketOrder.count(order == SortOrder.ASC)).subAggregation(topHits);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder).fetchSource(false);
        searchSourceBuilder.aggregation(aggregationBuilder);
        SearchRequest searchRequest = new SearchRequest(indices, searchSourceBuilder);
        try {
            log.info("exec elasticsearch count_by agg request:{}", searchRequest.source());
            try {
                SearchResponse searchResponse = getEsClient().search(searchRequest, RequestOptions.DEFAULT);
                ParsedTerms termAgg = (ParsedTerms) searchResponse.getAggregations().iterator().next();
                List<? extends Terms.Bucket> buckets = termAgg.getBuckets();
                if (!orderByCount) {
                    //reorder buckets by sortField
                    Collections.sort(buckets, new Comparator<Terms.Bucket>() {
                        private SortOrder order;

                        @Override
                        public int compare(Terms.Bucket o1, Terms.Bucket o2) {
                            SearchHit hit1 = ((ParsedTopHits) o1.getAggregations().iterator().next()).getHits().getHits()[0];
                            Comparable value1 = (Comparable) hit1.getSortValues()[0];
                            SearchHit hit2 = ((ParsedTopHits) o2.getAggregations().iterator().next()).getHits().getHits()[0];
                            Comparable value2 = (Comparable) hit2.getSortValues()[0];
                            int c = value1.compareTo(value2);
                            return order == SortOrder.ASC ? c : -c;
                        }

                        public Comparator<Terms.Bucket> build(SortOrder order) {
                            this.order = order;
                            return this;
                        }
                    }.build(order));
                }
                //pagination
                PageData<? extends Terms.Bucket> pageBuckets = filterBuckets(buckets, pageSize, page);
                pageBuckets.total = (long) buckets.size();
                //get doc by id
                List<String> ids = new ArrayList<>();
                pageBuckets.list.forEach(e -> {
                    ids.add(((ParsedTopHits) e.getAggregations().iterator().next()).getHits().getHits()[0].getId());
                });
                List<T> docs = this.mget(ids);
                //create result
                for (int i = 0; i < pageBuckets.list.size(); i++) {
                    T doc = docs.get(i);
                    Terms.Bucket bucket = pageBuckets.list.get(i);
                    result.list.add(DocCount.<T>builder().data(doc).count(bucket.getDocCount()).build());
                }
                result.total = pageBuckets.total;
                result.pageNum = pageBuckets.pageNum;
                result.pageSize = pageBuckets.pageSize;
            } catch (ElasticsearchStatusException e) {
                if (e.status() == RestStatus.NOT_FOUND) {
                    log.warn("indices not found. indices={}", indices);
                } else {
                    log.error("exec elasticsearch search failed. request:{}, e:{}", searchRequest, e);
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            log.error("exec elasticsearch search failed. request:{}, e:{}", searchRequest, e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public PageData<DocCount<T>> paginationGroupBy(BoolQueryBuilder boolQueryBuilder, String groupField, String sortField, boolean orderByCount, SortOrder order, int pageSize, int page) {
        return paginationGroupBy(boolQueryBuilder, groupField, sortField, orderByCount, order, pageSize, page, null, null, null, false);
    }

    private PageData<? extends Terms.Bucket> filterBuckets(List<? extends Terms.Bucket> all, int pageSize, int page) {
        PageData<Terms.Bucket> pageData = filterPage(pageSize, page);
        pageData.list = new ArrayList<>();
        for (int i = (page - 1) * pageSize; i < page * pageSize && i < all.size(); i++) {
            pageData.list.add(all.get(i));
        }
        return pageData;
    }

    public void refresh() {
        try {
            getEsClient().indices().refresh(new RefreshRequest(getWriteIndexAlias()), RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("refresh index failed. index={}, err={}", getIndexName(), e);
            e.printStackTrace();
        }
    }

    //TODO delete do not support multi indices. so this realiazation is not working
//    public void delete(String id) {
//        DeleteRequest deleteRequest = new DeleteRequest(getIndexPattern(), id);
//        try {
//            getEsClient().delete(deleteRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            log.error("delete doc by id failed. index={}, id={}, err={}", getIndexName(), id, e);
//            throw new RuntimeException(e);
//        }
//    }

    public void delete(String id) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termsQuery("_id", id));
        DeleteByQueryRequest req = new DeleteByQueryRequest();
        req.indices(this.getIndexPattern());
        req.setQuery(queryBuilder);
        try {
            BulkByScrollResponse resp = getEsClient().deleteByQuery(req, RequestOptions.DEFAULT);
            //TODO do with resp
        } catch (Exception e) {
            log.error("exec elasticsearch delete failed. request:{}, e:{}", req, e);
            throw new RuntimeException(e);
        }
    }

    public void bulkDelete(List<String> ids) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termsQuery("_id", ids));
        DeleteByQueryRequest req = new DeleteByQueryRequest();
        req.indices(this.getIndexPattern());
        req.setQuery(queryBuilder);
        try {
            BulkByScrollResponse resp = getEsClient().deleteByQuery(req, RequestOptions.DEFAULT);
            //TODO do with resp
        } catch (Exception e) {
            log.error("exec elasticsearch bulk delete failed. request:{}, e:{}", req, e);
            throw new RuntimeException(e);
        }
    }

    public T get(String id) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("_id", id));
        List<T> list = this.search(queryBuilder, null, SortOrder.DESC, 1, 1);
        if (list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }


    public List<T> mget(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("_id", ids));
        List<T> list = this.scrollSearch(queryBuilder, null, null, null, null, null, ids.size());
//        List<T> list = this.search(queryBuilder, null, null, getMaxResultWindow(), 1);
        HashMap<String, T> map = new HashMap<>();
        list.forEach(e -> {
            map.put(e.id, e);
        });
        List<T> result = new ArrayList<>();
        ids.forEach(id -> {
            T e = map.get(id);
            if (e == null) {
                //TODO new errorcode type
                throw new BusinessException(BusinessResponseEnum.RECORD_NOT_EXISTS.getCode(), BusinessResponseEnum.RECORD_NOT_EXISTS.getMsg());
            }
            result.add(e);
        });
        return result;
    }

    public long count() {
        return count(QueryBuilders.boolQuery());
    }

    public long count(BoolQueryBuilder boolQueryBuilder) {
        return count(boolQueryBuilder, null, null, null, false);
    }

    public long count(BoolQueryBuilder boolQueryBuilder, String timeField, OffsetDateTime timeFrom, OffsetDateTime timeTo, boolean reduceIndicesScope) {
        String[] indices = addTimeQuery(boolQueryBuilder, timeField, timeFrom, timeTo);
        if (!reduceIndicesScope) {
            indices = new String[]{getIndexPattern()};
        }
        CountRequest countRequest = new CountRequest(indices, boolQueryBuilder);
        try {
            log.info("exec elasticsearch count request:{}", countRequest);
            try {
                CountResponse countResponse = getEsClient().count(countRequest, RequestOptions.DEFAULT);
                return countResponse.getCount();
            } catch (ElasticsearchStatusException e) {
                if (e.status() == RestStatus.NOT_FOUND) {
                    log.warn("indices not found. indices={}", indices);
                    return 0;
                } else {
                    log.error("exec elasticsearch count failed. request:{}, e:{}", countRequest, e);
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            log.error("exec elasticsearch count failed. request:{}, e:{}", countRequest, e);
            throw new RuntimeException(e);
        }
    }

    public List<T> search(BoolQueryBuilder boolQueryBuilder, String sortField, SortOrder order, int pageSize, int page) {
        return search(boolQueryBuilder, sortField, order, pageSize, page, null, null, null, false);
    }

    public List<T> search(BoolQueryBuilder boolQueryBuilder, String sortField, SortOrder order, int pageSize, int page, String timeField, OffsetDateTime timeFrom, OffsetDateTime timeTo, boolean reduceIndicesScope) {
        if (sortField == null || sortField.equals("")) {
            sortField = "gmtCreate";
        }
        if (order == null) {
            order = SortOrder.DESC;
        }
        PageData<T> tmp = filterPage(pageSize, page);
        pageSize = tmp.pageSize;
        page = tmp.pageNum;
        String[] indices = addTimeQuery(boolQueryBuilder, timeField, timeFrom, timeTo);
        if (!reduceIndicesScope) {
            indices = new String[]{getIndexPattern()};
        }
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        ssb.sort(sortField, order).size(pageSize).from((page - 1) * pageSize).query(boolQueryBuilder);
        SearchRequest searchRequest = new SearchRequest(indices, ssb);
        try {
            log.info("exec elasticsearch search request:{}", searchRequest.source());
            try {
                SearchResponse searchResponse = getEsClient().search(searchRequest, RequestOptions.DEFAULT);
                SearchHit[] hits = searchResponse.getHits().getHits();
                List<T> data = new ArrayList<>();
                for (SearchHit hit : hits) {
                    data.add(respToEntity(hit.getId(), hit.getSourceRef()));
                }
                return data;
            } catch (ElasticsearchStatusException e) {
                if (e.status() == RestStatus.NOT_FOUND) {
                    log.warn("indices not found. indices={}", indices);
                    return new ArrayList<>();
                } else {
                    log.error("exec elasticsearch search failed. request:{}, e:{}", searchRequest, e);
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            log.error("exec elasticsearch search failed. request:{}, e:{}", searchRequest, e);
            throw new RuntimeException(e);
        }
    }

    public List<T> scrollSearch(BoolQueryBuilder boolQueryBuilder, String sortField, SortOrder order, String timeField, OffsetDateTime timeFrom, OffsetDateTime timeTo, int limit) {
        return this.scrollSearch(boolQueryBuilder, sortField, order, timeField, timeFrom, timeTo, limit, false, true, null);
    }

    public List<T> scrollSearch(BoolQueryBuilder boolQueryBuilder, String sortField, SortOrder order, String timeField, OffsetDateTime timeFrom, OffsetDateTime timeTo, int limit, boolean reduceIndicesScope, boolean fetchAllSource, String[] sourceIncludes) {
        List<T> result = new ArrayList<>();
        if (sortField == null || sortField.equals("")) {
            sortField = "gmtCreate";
        }
        if (order == null) {
            order = SortOrder.DESC;
        }
        String[] indices = addTimeQuery(boolQueryBuilder, timeField, timeFrom, timeTo);
        if (!reduceIndicesScope) {
            indices = new String[]{getIndexPattern()};
        }
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        ssb.sort(sortField, order).size(SCROLL_SEARCH_WINDOW).query(boolQueryBuilder);
        if (!fetchAllSource) {
            ssb.fetchSource(sourceIncludes, null);
        }
        SearchRequest searchRequest = new SearchRequest(indices, ssb);
        searchRequest.scroll(TimeValue.timeValueSeconds(SCROLL_SEARCH_TIMEOUT_SECONDS));
        try {
            //scroll search
            log.info("exec elasticsearch scroll search searchRequest:{}", searchRequest.source());
            try {
                SearchResponse searchResponse = getEsClient().search(searchRequest, RequestOptions.DEFAULT);
                Set<String> scrollIdSet = new HashSet<>();
                String scrollId = searchResponse.getScrollId();
                scrollIdSet.add(scrollId);
                SearchHit[] hits = searchResponse.getHits().getHits();
                for (SearchHit hit : hits) {
                    result.add(respToEntity(hit.getId(), hit.getSourceRef()));
                }
                while (hits.length > 0 && (limit <= 0 || result.size() < limit)) {
                    log.info("search scrolling. hits={}, totalHits={}", result.size(), searchResponse.getHits().getTotalHits());
                    SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                    scrollRequest.scroll(TimeValue.timeValueSeconds(SCROLL_SEARCH_TIMEOUT_SECONDS));
                    searchResponse = getEsClient().scroll(scrollRequest, RequestOptions.DEFAULT);
                    scrollId = searchResponse.getScrollId();
                    scrollIdSet.add(scrollId);
                    hits = searchResponse.getHits().getHits();
                    for (SearchHit hit : hits) {
                        result.add(respToEntity(hit.getId(), hit.getSourceRef()));
                    }
                }
                //clear scroll context avoid es to exceed max scroll context limitation
                ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
                List<String> scrollIds = new ArrayList<>();
                scrollIds.addAll(scrollIdSet);
                clearScrollRequest.setScrollIds(scrollIds);
                getEsClient().clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
                if (limit >= 0) {
                    int size = limit <= result.size() ? limit : result.size();
                    result = result.subList(0, size);
                }
                return result;

            } catch (ElasticsearchStatusException e) {
                if (e.status() == RestStatus.NOT_FOUND) {
                    log.warn("indices not found. indices={}", indices);
                    return new ArrayList<>();
                } else {
                    log.error("exec elasticsearch scroll search failed. searchRequest:{}, e:{}", searchRequest, e);
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            log.error("exec elasticsearch scroll search failed. searchRequest:{}, e:{}", searchRequest, e);
            throw new RuntimeException(e);
        }
    }

    private <T> PageData<T> filterPage(int pageSize, int page) {
        PageData<T> result = new PageData<>();
        result.pageSize = pageSize <= 0 ? 10 : pageSize;
        result.pageNum = page <= 0 ? 1 : page;
        return result;
    }

    private long calcTotalPage(int pageSize, long elemCount) {
        long total = elemCount / pageSize;
        if (total * pageSize < elemCount) {
            total++;
        }
        return total;
    }

    private T respToEntity(String id, BytesReference resp) {
        T entity = JSON.parseObject(resp.utf8ToString(), docClass);
        entity.id = id;
        return entity;
    }

    private String[] addTimeQuery(BoolQueryBuilder boolQueryBuilder, String timeField, OffsetDateTime timeFrom, OffsetDateTime timeTo) {
        if (timeField != null) {
            RangeQueryBuilder rq = QueryBuilders.rangeQuery(timeField);
            if (timeFrom != null) {
                rq = rq.from(timeFrom, true);
            }
            if (timeTo != null) {
                rq = rq.to(timeTo, false);
            }
            boolQueryBuilder.must(rq);
        }
        return getIndicesOfTimeRange(timeFrom, timeTo);
    }

    //TODO 如果以后性能有问题的话，把索引过滤的精确一点
    private String[] getIndicesOfTimeRange(OffsetDateTime timeFrom, OffsetDateTime timeTo) {
        String wildcard = getIndexPattern();
        //TODO 万一有数据不在正确的时间索引里，就查不到了，先从所有索引里查好了，等性能测试的时候看看行不行
//        int y1 = timeFrom != null ? timeFrom.getYear() : 0;
//        int y2 = timeTo != null ? timeTo.getYear() : 3000;
//        if (y1 == y2) {
//            int m1 = timeFrom != null ? timeFrom.getMonthValue() : 1;
//            int m2 = timeTo != null ? timeTo.getMonthValue() : 12;
//            if (m1 == m2) {
//                int d1 = timeFrom != null ? timeFrom.getDayOfMonth() : 1;
//                int d2 = timeTo != null ? timeTo.getDayOfMonth() : 31;
//                if (d1 == d2) {
//                    //linyue-test-2020.04.13-000001
//                    wildcard = String.format("%s-%04d.%02d.%02d-*", getIndexName(), y1, m1, d1);
//                } else {
//                    wildcard = String.format("%s-%04d.%02d.*", getIndexName(), y1, m1);
//                }
//            } else {
//                wildcard = String.format("%s-%04d.*", getIndexName(), y1);
//            }
//        } else {
//            wildcard = String.format("%s-*", getIndexName());
//        }
        log.debug("get indices of time range. wildcard={}", wildcard);
        return new String[]{wildcard};
    }

    protected abstract RestHighLevelClient getEsClient();

    protected abstract int getShards();

    protected abstract int getReplicas();

    protected abstract int getMaxResultWindow();

    protected abstract int getRolloverDays();

    protected abstract long getRolloverDocCount();

    protected abstract long periodInsertMillionSeconds();

    protected abstract long periodInsertMaxCount();

    private String getIndexName() {
        return indexName;
    }

    protected String getIndexPattern() {
        return this.getIndexName() + "-*";
    }

    protected String getWriteIndexAlias() {
        return this.getIndexName() + "_w";
    }

    private String getUtcOffset() {
        ZoneId zoneId = ZoneId.systemDefault();
        return String.format("%tz", Instant.now().atZone(zoneId));
    }

    private boolean indexExists() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(getWriteIndexAlias());
        return getEsClient().indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    private boolean compareTemplate(PutIndexTemplateRequest put, IndexTemplateMetaData old) {
        if (!put.settings().equals(old.settings())) {
            return false;
        }
        String jsonMappingNew = put.mappings().utf8ToString();
        String jsonMappingOld = new String(old.mappings().source().uncompressed());
        if (!compareMapping(jsonMappingNew, jsonMappingOld)) {
            return false;
        }
        return true;
    }

    private boolean compareMapping(String jsonMappingNew, String jsonMappingOld) {
        JSONObject mappingNew = JSON.parseObject(jsonMappingNew);
        JSONObject mappingOld = JSON.parseObject(jsonMappingOld);
        log.info("compare mappings. old={}, new={}", jsonMappingOld, jsonMappingNew);
        return mappingNew.equals(mappingOld);
    }


    private String requestToLogString(ToXContent request) throws IOException {
        BytesReference source = XContentHelper.toXContent(request, XContentType.JSON, true);
        return source.utf8ToString();
    }

    private String requestToLogString(BytesReference request) throws IOException {
        return request.utf8ToString();
    }
}