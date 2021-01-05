package com.search.sync;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.search.biz.OriginVoiceParseService;
import com.search.biz.SelectValueService;
import com.search.common.utils.BigDecimalUtils;
import com.search.common.utils.R;
import com.search.entity.SysArticleEntity;
import com.search.vo.QueryVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedStats;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class ElasticsearchUtils {

    private static final String INDEX_NAME = "newindex";

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    SelectValueService selectValueService;

    @Autowired
    OriginVoiceParseService originVoiceParseService;

    @Autowired
    ParseResultService parseResultService;


    public String getElasticIndexLastInsertId(String indexName,Class<?> ... classes){
        try {
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from(0).size(1).sort("id", SortOrder.DESC);
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            SearchSourceBuilder query = searchSourceBuilder.query(matchAllQueryBuilder);
            matchAllQueryBuilder.queryName("id");
            searchRequest.source(query);
            SearchResponse search = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
            Optional<SearchHit> first = Arrays.stream(search.getHits().getHits()).findFirst();
            if(!first.isPresent()){
                log.error("没有查询到结果");
                return "";
            }
            Object id = first.get().getSourceAsMap().get("id");
            return id.toString();
        } catch (IOException e) {
            log.error("查询es 时出现异常",e);
            return "";
        }
    }

    public int syncDatabaseToElasticsearchBulk(String indexName,List<SysArticleEntity> list){
        log.info("开始同步数据库中的数据到 elasticsearch:"+list.size());
        try {
            final BulkRequest bulkRequest = new BulkRequest();
            for (int i = 0; i < list.size(); i++) {
                SysArticleEntity sysArticleEntity = list.get(i);
                IndexRequest request = new IndexRequest(indexName);
                bulkRequest.add(
                    request.id(String.valueOf(i)).source(JSON.toJSONString(sysArticleEntity), XContentType.JSON)
                );
            }
            final BulkResponse index = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if(index.hasFailures()){
                return 0;
            }
            return index.getItems().length;
        } catch (Exception e) {
            log.error("本次同步数据库数据到elasticsearch 中失败",e);
            return 0;
        }
    }

    public R justForContent(QueryVO queryVO,String indexName){
        R simpleCheck = simpleCheck(queryVO);
        if(!simpleCheck.isSuccess()){
            return simpleCheck;
        }
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);

        try {
            final BoolQueryBuilder boolQueryBuilder = buildMustFilterQuery(queryVO);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.from(Objects.isNull(queryVO.getPageSize())?0:queryVO.getPageSize());
            sourceBuilder.size(Objects.isNull(queryVO.getPageNumber())?10:queryVO.getPageNumber());
            if(queryVO.getOrderBy()==null || queryVO.getOrderBy() == 0){
                sourceBuilder.sort("insertTime",SortOrder.DESC);
            }else {
                sourceBuilder.sort("publisherPv",SortOrder.DESC);
            }
            searchRequest.source(sourceBuilder);
            final SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            final JSONObject object = parseSimpleSearch(search,queryVO);
            System.out.println(search);
            return R.ok(object);
        } catch (IOException e) {
            log.error("查询滚动条失败",e);
            return R.error("查询出错");
        }
    }
    private JSONObject parseSimpleSearch(SearchResponse search,QueryVO queryVO){
        JSONObject obj = new JSONObject();
        if(search.status() != RestStatus.OK){
            JSONObject object = new JSONObject();
            object.put("msg","查询异常");
            return object;
        }
        final SearchHits hits = search.getHits();
        List<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            list.add(hit.getSourceAsMap());
        }
        obj.put("data",list);
        obj.put("pageNumber",Objects.isNull(queryVO.getPageNumber()) ? 0 : queryVO.getPageNumber());
        obj.put("pageSize",Objects.isNull(queryVO.getPageSize()) ? 10 : queryVO.getPageSize());
        return obj;
    }


    /**
     * 简单查询 没按照产品 品牌分组
     * @param queryVO 查询条件 from 0 概览页 、1 分析页、2：对比页
     * @param indexName 索引名
     * @return 返回结果
     */
    public R doSingleSearch(QueryVO queryVO,String indexName){
        R simpleCheck = simpleCheck(queryVO);
        if(!simpleCheck.isSuccess()){
            return simpleCheck;
        }
        if(Objects.nonNull(queryVO.getFrom())&& queryVO.getFrom()==2){
            if(!CollectionUtils.isEmpty(queryVO.getTitleIds())){
                return this.doMultiSearch(queryVO,indexName);
            }
        }
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        try {

            final BoolQueryBuilder boolQueryBuilder = buildMustFilterQuery(queryVO);
            SearchSourceBuilder sourceBuilder = this.aggRouter(queryVO,boolQueryBuilder);
            if(Objects.isNull(sourceBuilder)){
                return R.error("构建查询失败");
            }
            searchRequest.source(sourceBuilder);
            System.out.println("sourceBuilder======="+sourceBuilder);
            System.out.println("boolQueryBuilder====="+sourceBuilder);
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(search);
            JSONObject obj = parseResultService.parseElasticsearchResponse(search,queryVO);
            return R.ok(obj);
        } catch (Exception e) {
            JSONObject object = new JSONObject();
            object.put("error",indexName);
            object.put("msg","查询异常");
            object.put("request",JSON.toJSONString(queryVO));
            final R error = R.error();
            error.setData(object);
            return error;
        }
    }

    private R doMultiSearch(QueryVO queryVO, String indexName) {
        Map<String,List<JSONObject>> map = new HashMap<>(8);
        final List<Integer> titleIds = queryVO.getTitleIds();
        List<QueryVO> list = new ArrayList<>();
        try {
            for (int i = 0; i < titleIds.size(); i++) {
                final QueryVO one = new QueryVO();
                BeanUtil.copyProperties(queryVO,one);
                one.setTitleIds(new ArrayList<>());
                one.setTitleId(titleIds.get(i));
                one.setFrom(0);
                list.add(one);
            }
            List<JSONObject> resultOk = new ArrayList<>();
            List<JSONObject> resultError = new ArrayList<>();
            for (QueryVO vo : list) {
                final R r = this.doSingleSearch(vo, indexName);
                if(!r.isSuccess()){
                    JSONObject temp = (JSONObject) r.getData();
                    temp.put("titleId",vo.getTitleId());
                    resultError.add(temp);
                }
                JSONObject temp = (JSONObject) r.getData();
                temp.put("titleId",vo.getTitleId());
                resultOk.add(temp);
            }
            map.put("success",resultOk);
            map.put("fail",resultError);
            return R.ok(map);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    private R simpleCheck(QueryVO queryVO) {
        if(Objects.isNull(queryVO)){
            return R.error("查询失败");
        }
//        if(Objects.isNull(queryVO.getFrom())){
//            return R.error("查询地点位置");
//        }
        if(Objects.isNull(queryVO.getTitleId())&&Objects.isNull(queryVO.getTitleIds())){
            return R.error("未知的品牌产品查询");
        }
        return R.ok();
    }

    private SearchSourceBuilder aggRouter(QueryVO queryVO,BoolQueryBuilder boolQueryBuilder){
        if(queryVO.getFrom()==0){
            return buildDetailAgg(queryVO,boolQueryBuilder);
        }else if(queryVO.getFrom() == 1){
            return buildAnalysisAgg(queryVO,boolQueryBuilder);
        }else {
            return buildCompareAgg(queryVO,boolQueryBuilder);
        }
    }

    private SearchSourceBuilder buildCompareAgg(QueryVO queryVO, BoolQueryBuilder boolQueryBuilder) {
        return buildDetailAgg(queryVO,boolQueryBuilder);
    }

    private SearchSourceBuilder buildAnalysisAgg(QueryVO queryVO, BoolQueryBuilder boolQueryBuilder) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        final FilterAggregationBuilder aggregationBuilder = AggregationBuilders.filter("boolFilter", boolQueryBuilder);

        DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram("insertTime");
        dateHistogram.field("insertTime").calendarInterval(DateHistogramInterval.days(1));
        TermsAggregationBuilder mediaType = AggregationBuilders.terms("emotionType").field("emotionType");

        final TermsAggregationBuilder topicId = AggregationBuilders.terms("topicId").field("topicId").size(1024*1024);
        dateHistogram.subAggregation(mediaType);
        dateHistogram.subAggregation(topicId);
        aggregationBuilder.subAggregation(dateHistogram);
        sourceBuilder.aggregation(aggregationBuilder);
        return sourceBuilder;
    }

    private SearchSourceBuilder buildDetailAgg(QueryVO queryVO,BoolQueryBuilder boolQueryBuilder) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        final FilterAggregationBuilder aggregationBuilder = AggregationBuilders.filter("boolFilter", boolQueryBuilder);
        DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram("insertTime");
        dateHistogram.field("insertTime").calendarInterval(DateHistogramInterval.days(1));
        TermsAggregationBuilder mediaType = AggregationBuilders.terms("mediaType");
        mediaType.field("mediaType");

        TermsAggregationBuilder terms = AggregationBuilders.terms("publisherSiteName").field("publisherSiteName");

        AggregationBuilder publisherRepostNumStatAgg = AggregationBuilders.stats("publisherRepostNum").field("publisherRepostNum");
        AggregationBuilder publisherLinkNumStatAgg = AggregationBuilders.stats("publisherLinkNum").field("publisherLinkNum");
        AggregationBuilder publisherPvStatAgg = AggregationBuilders.stats("publisherPv").field("publisherPv");
        AggregationBuilder publisherCommentNumStatAgg = AggregationBuilders.stats("publisherCommentNum").field("publisherCommentNum");
        AggregationBuilder publisherCollectionNumStatAgg = AggregationBuilders.stats("publisherCollectionNum").field("publisherCollectionNum");
        terms.subAggregation(publisherRepostNumStatAgg);
        terms.subAggregation(publisherLinkNumStatAgg);
        terms.subAggregation(publisherPvStatAgg);
        terms.subAggregation(publisherCommentNumStatAgg);
        terms.subAggregation(publisherCollectionNumStatAgg);
        mediaType.subAggregation(terms);
        dateHistogram.subAggregation(mediaType);
        aggregationBuilder.subAggregation(dateHistogram);
        sourceBuilder.aggregation(aggregationBuilder);
        return sourceBuilder;
    }


    /**
     * 这里目前只做第一张图和第二张图的查询
     * @param queryVO 查询条件
     * @return 返回查询结果
     */
    public BoolQueryBuilder buildMustFilterQuery(QueryVO queryVO){
        log.info("构建查询过滤器，原始数据为：{}",JSON.toJSONString(queryVO));
        final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if(Objects.nonNull(queryVO.getBannerUse())&&queryVO.getBannerUse()){
            log.info("banner 对比不用此字段过滤");
        }else{
            boolQuery.must(QueryBuilders.termQuery("titleId",queryVO.getTitleId()));
        }
        if(Objects.nonNull(queryVO.getStartDate())&&Objects.nonNull(queryVO.getEndDate())){
            boolQuery.must(QueryBuilders.rangeQuery("insertTime").gte(queryVO.getStartDate().getTime()).lte(queryVO.getEndDate().getTime()));
        }
        if(!CollectionUtils.isEmpty(queryVO.getEmotionList())){
            final int size = queryVO.getEmotionList().size();
            if( size!= 0 &&  size != 3){
                BoolQueryBuilder emotionBool = QueryBuilders.boolQuery();
                log.info("情感类型过滤；0：中性、1：负面、2：正面");
                List<Integer> emotionList = queryVO.getEmotionList();
                for (Integer integer : emotionList) {
                    emotionBool.should(QueryBuilders.termQuery("emotionType",integer));
                }
                boolQuery.must(emotionBool);
            }
        }
        if(!CollectionUtils.isEmpty(queryVO.getContentList())){
            final int size = queryVO.getContentList().size();
            if( size!= 0 &&  size != 3){
                log.info("内容类型过滤；0、全部：1、UGC:2:PGC");
                BoolQueryBuilder contentBool = QueryBuilders.boolQuery();
                List<Integer> contentList = queryVO.getContentList();
                for (Integer integer : contentList) {
                    contentBool.should(QueryBuilders.termQuery("contentId",integer));
                }
                boolQuery.must(contentBool);
            }
        }
        if(!CollectionUtils.isEmpty(queryVO.getTopicList()) ){
            final int size = queryVO.getTopicList().size();
            if( size!= 0){
                log.info("话题类型过滤；");
                final BoolQueryBuilder topicBool = QueryBuilders.boolQuery();
                List<Integer> topicList = queryVO.getTopicList();
                for (Integer integer : topicList) {
                    topicBool.should(QueryBuilders.wildcardQuery("topicId","*" +integer +"*"));
                }
                boolQuery.must(topicBool);
            }
        }
        if(!CollectionUtils.isEmpty(queryVO.getMediaList()) ){
            final int size = queryVO.getMediaList().size();
            if( size!= 0 &&  size != 6){
                log.info("媒体类型过滤；0：微信；1：微博；2：博客；3：论坛：4：问答；5：新闻");
                List<Integer> mediaList = queryVO.getMediaList();
                final BoolQueryBuilder mediaQuery = QueryBuilders.boolQuery();
                for (Integer integer : mediaList) {
                    mediaQuery.should(QueryBuilders.termQuery("mediaType",integer));
                }
                boolQuery.must(mediaQuery);
            }
        }
        if(!CollectionUtils.isEmpty(queryVO.getIncludeList())){
            List<String> includeList = queryVO.getIncludeList();
            for (String str : includeList) {
                boolQuery.must(QueryBuilders.wildcardQuery("content", "*" + str + "*"));
            }
        }
        if(!CollectionUtils.isEmpty(queryVO.getExcludeList())){
            List<String> excludeList = queryVO.getExcludeList();
            for (String str : excludeList) {
                boolQuery.mustNot(QueryBuilders.wildcardQuery("content", "*" + str + "*"));
            }
        }
        return boolQuery;
    }

    public R doOriginVoiceQuery(QueryVO queryVO,String indexName) {
        R simpleCheck = simpleCheck(queryVO);
        if(!simpleCheck.isSuccess()){
            return simpleCheck;
        }
        if(queryVO.getMediaType() == null){
            return R.error("需要传入声量来源分析具体的媒体类型");
        }
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        try {

            final BoolQueryBuilder boolQueryBuilder = buildMustFilterQuery(queryVO);
            boolQueryBuilder.must(QueryBuilders.termQuery("mediaType",queryVO.getMediaType()));
            SearchSourceBuilder sourceBuilder = this.buildOriginQuery(queryVO,boolQueryBuilder);
            searchRequest.source(sourceBuilder);
            log.info("sourceBuilder======="+sourceBuilder);
            log.info("boolQueryBuilder====="+sourceBuilder);
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            JSONObject obj = originVoiceParseService.parseElasticsearchResponse(search,queryVO);
            return R.ok(obj);
        } catch (Exception e) {
            JSONObject object = new JSONObject();
            object.put("error",indexName);
            object.put("msg","查询异常");
            object.put("request",JSON.toJSONString(queryVO));
            final R error = R.error();
            error.setData(object);
            return error;
        }
    }

    private SearchSourceBuilder buildOriginQuery(QueryVO queryVO, BoolQueryBuilder boolQueryBuilder) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        final FilterAggregationBuilder aggregationBuilder = AggregationBuilders.filter("boolFilter", boolQueryBuilder);

        TermsAggregationBuilder terms = AggregationBuilders.terms("publisherSiteName").field("publisherSiteName");

        AggregationBuilder publisherRepostNumStatAgg = AggregationBuilders.stats("publisherRepostNum").field("publisherRepostNum");
        AggregationBuilder publisherLinkNumStatAgg = AggregationBuilders.stats("publisherLinkNum").field("publisherLinkNum");
        AggregationBuilder publisherPvStatAgg = AggregationBuilders.stats("publisherPv").field("publisherPv");
        AggregationBuilder publisherCommentNumStatAgg = AggregationBuilders.stats("publisherCommentNum").field("publisherCommentNum");
        AggregationBuilder publisherCollectionNumStatAgg = AggregationBuilders.stats("publisherCollectionNum").field("publisherCollectionNum");
        terms.subAggregation(publisherRepostNumStatAgg);
        terms.subAggregation(publisherLinkNumStatAgg);
        terms.subAggregation(publisherPvStatAgg);
        terms.subAggregation(publisherCommentNumStatAgg);
        terms.subAggregation(publisherCollectionNumStatAgg);

        TermsAggregationBuilder field = AggregationBuilders.terms("publisher").field("publisher");
        field.subAggregation(publisherRepostNumStatAgg);
        field.subAggregation(publisherLinkNumStatAgg);
        field.subAggregation(publisherPvStatAgg);
        field.subAggregation(publisherCommentNumStatAgg);
        field.subAggregation(publisherCollectionNumStatAgg);
        final TermsAggregationBuilder publisherUrlAgg = AggregationBuilders.terms("publisherUrl").field("publisherUrl").size(1);
        field.subAggregation(publisherUrlAgg);

        aggregationBuilder.subAggregation(terms);
        aggregationBuilder.subAggregation(field);

        sourceBuilder.aggregation(aggregationBuilder);
        return sourceBuilder;
    }

    public R getBannerCompareData(QueryVO queryVO,String indexName){
        JSONObject object = new JSONObject();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        try {
            final SearchSourceBuilder searchSourceBuilder = buildBannerCompareQuery(queryVO);
            searchRequest.source(searchSourceBuilder);
            final SearchResponse search = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if(search.status() != RestStatus.OK){
                return R.error("查询失败");
            }
            Map<String, Aggregation> aggregations = search.getAggregations().asMap();
            ParsedFilter parsedFilter = (ParsedFilter) aggregations.get("boolFilter");
            if(Objects.isNull(parsedFilter)){
                return  R.error("查询失败");
            }
            Map<String, Aggregation> filtered = parsedFilter.getAggregations().asMap();
            if(Objects.isNull(filtered)){
                return  R.error("查询失败");
            }
            ParsedStringTerms publisherSiteNameTerms = (ParsedStringTerms)filtered.get("titleId");
            final List<? extends Terms.Bucket> buckets = publisherSiteNameTerms.getBuckets();
            // 当前的titleid
            final Integer titleId = queryVO.getTitleId();
            // 当前id 文章数
            long curTitleIdCount = 0L;
            // 所有文章总数
            long totalCount = 0L;
            // 总声量
            String totalSumAll = "0";
            // 当前总声量
            String  curTotalSumAll = "0";
            // 平均值
            for (Terms.Bucket bucket : buckets) {
                if(Integer.parseInt(bucket.getKeyAsString()) == titleId){
                    curTitleIdCount += bucket.getDocCount();
                }
                final Aggregations bucketAggregations = bucket.getAggregations();
                final List<Aggregation> aggregations1 = bucketAggregations.asList();
                for (Aggregation aggregation : aggregations1) {
                    ParsedStats parsedStats = (ParsedStats) aggregation;
                    final String round = BigDecimalUtils.divRound2(parsedStats.getSum(),1, 0);
                    totalSumAll = BigDecimalUtils.add(totalSumAll,round);
                    if(Integer.parseInt(bucket.getKeyAsString()) == titleId){
                        curTotalSumAll = BigDecimalUtils.add(curTotalSumAll,round);
                    }
                }
                totalCount += bucket.getDocCount();
            }
            // 声量平均值
            final String s = BigDecimalUtils.divRound2(totalCount, buckets.size(), 5);
            object.put("vsHigh", BigDecimalUtils.divRound2(BigDecimalUtils.reduce(curTitleIdCount,s),s,5));
            final String s1 = BigDecimalUtils.divRound2(totalSumAll, buckets.size(), 5);
            // 总互动量环比
            object.put("xx",BigDecimalUtils.divRound2(BigDecimalUtils.reduce(curTitleIdCount,s1),s1,5));
            // 上一期环比
            object.put("xx2",curTotalSumAll);
            return R.ok(object);
        }catch (Exception e){
            log.error("查询环比失败",e);
            return R.error("查询环比失败");
        }
    }

    private  SearchSourceBuilder buildBannerCompareQuery(QueryVO queryVO){
        queryVO.setBannerUse(true);
        BoolQueryBuilder boolQueryBuilder = buildMustFilterQuery(queryVO);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        final FilterAggregationBuilder aggregationBuilder = AggregationBuilders.filter("boolFilter", boolQueryBuilder);

        TermsAggregationBuilder terms = AggregationBuilders.terms("titleId").field("titleId");
        AggregationBuilder publisherRepostNumStatAgg = AggregationBuilders.stats("publisherRepostNum").field("publisherRepostNum");
        AggregationBuilder publisherLinkNumStatAgg = AggregationBuilders.stats("publisherLinkNum").field("publisherLinkNum");
        AggregationBuilder publisherPvStatAgg = AggregationBuilders.stats("publisherPv").field("publisherPv");
        AggregationBuilder publisherCommentNumStatAgg = AggregationBuilders.stats("publisherCommentNum").field("publisherCommentNum");
        AggregationBuilder publisherCollectionNumStatAgg = AggregationBuilders.stats("publisherCollectionNum").field("publisherCollectionNum");
        terms.subAggregation(publisherRepostNumStatAgg);
        terms.subAggregation(publisherLinkNumStatAgg);
        terms.subAggregation(publisherPvStatAgg);
        terms.subAggregation(publisherCommentNumStatAgg);
        terms.subAggregation(publisherCollectionNumStatAgg);

        aggregationBuilder.subAggregation(terms);
        sourceBuilder.aggregation(aggregationBuilder);
        return sourceBuilder;
    }

}
