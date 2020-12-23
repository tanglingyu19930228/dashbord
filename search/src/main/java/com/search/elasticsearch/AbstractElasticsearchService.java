package com.search.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.search.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 基础的操作Elasticsearch服务类，其他的实现类都应该继承自这个类
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high.html
 * @author liujianguo
 */
@Slf4j
public abstract class AbstractElasticsearchService {
    public static final String UNDER_LINE = "_";
    /** 设定分页查询结果集的最大条数 */
    public static final int MAX_RESULT_WINDOW = 500000;

    /** 默认的分片数 */
    public static final int DEFAULT_SHARDS = 5;

    /** 默认的副本数 */
    private static final int DEFAULT_REPLICAS = 1;

    private RestHighLevelClient restHighLevelClient;

    public AbstractElasticsearchService(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public String index(Doc doc) {
        createIndex();
        IndexRequest indexRequest = new IndexRequest(physicalIndex());
        indexRequest.source(JSONObject.toJSONString(doc), XContentType.JSON);
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            return response.getId();
        } catch (IOException ex) {
            log.error("add doc to es failed. doc:{}, indexName:{}, ex:{}", doc, physicalIndex(), ex);
            throw new RuntimeException(ex);
        }
    }

    public void delete(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(logicIndex(), id);
        try {
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("delete doc by id failed. id:{}, ex:{}", id, e);
            throw new RuntimeException(e);
        }
    }

    public <R> R get(String id, Function<GetResponse, R> responseHandler) {
        List<String> indices = getIndicesByAlias(logicIndex());
        for (String index: indices) {
            GetRequest getRequest = new GetRequest(index, id);
            try {
                GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
                log.info("get doc by id. id:{}, index:{}, response:{}", id, logicIndex(), getResponse);
                if (getResponse.isExists()) {
                    return responseHandler.apply(getResponse);
                }
            } catch (IOException e) {
                log.error("find doc by id failed. logicIndex:{}, physicalIndex:{}, id", logicIndex(), physicalIndex(), id);
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public <R> List<R> mget(List<String> idList, Function<MultiGetResponse, List<R>> responseHandler) {
        if (idList == null || idList.isEmpty()) {
            return null;
        }
        try {
            MultiGetRequest request = new MultiGetRequest();
            idList.forEach(id -> request.add(logicIndex(), id));
            MultiGetResponse responses = restHighLevelClient.mget(request, RequestOptions.DEFAULT);
            return responseHandler.apply(responses);
        } catch (IOException e) {
            log.error("mget doc by idList failed. idList:{}", idList);
            throw new RuntimeException(e);
        }
    }

    public void update(String id, Doc doc) {
        UpdateRequest updateRequest = new UpdateRequest(logicIndex(), id);
        updateRequest.doc(JSON.toJSONString(doc), XContentType.JSON);
        try {
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("update doc by id failed. index:{}, id:{}, doc:{}", physicalIndex(), id, doc);
            throw new RuntimeException(e);
        }
    }


    public <R> R search(Supplier<List<String>> indexSupplier, Supplier<SearchSourceBuilder> builderSupplier, Function<SearchResponse, R> responseHandler) {
        return search(indexSupplier.get(), builderSupplier.get(), responseHandler);
    }

    /**
     * 通用的数据检索方法
     * 参考文档：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-search.html
     * @param indices 需要检索的索引列表
     * @param searchSourceBuilder 数据查询构建器
     * @param responseHandler 返回结果处理器
     * @param <R> 返回结果对象泛型
     * @return 返回处理后的结果对象
     */
    public <R> R search(List<String> indices, SearchSourceBuilder searchSourceBuilder, Function<SearchResponse, R> responseHandler) {
        if (indices == null || indices.isEmpty()) {
            log.error("search indices is empty.");
            return null;
        }

        // 组装查询请求
        SearchRequest request = new SearchRequest(indices.toArray(new String[indices.size()]));
        request.source(searchSourceBuilder);
        log.info("exec elasticsearch query request:{}", request);

        // 执行查询并处理响应对象
        try {
            SearchResponse response = getRestHighLevelClient().search(request, RequestOptions.DEFAULT);
            if (response == null) {
                return null;
            }
            return responseHandler.apply(response);
        } catch (IOException e) {
            log.error("exec elasticsearch query failed. request:{}, e:{}", request, e);
            throw new RuntimeException(e);
        }
    }

    public Long count(List<String> indices, QueryBuilder queryBuilder) {
        if (indices == null || indices.isEmpty()) {
            log.error("count indices is empty.");
            return 0L;
        }

        CountRequest countRequest = new CountRequest(indices.toArray(new String[indices.size()]), queryBuilder);
        try {
            log.info("exec elasticsearch count request:{}", countRequest);
            CountResponse countResponse = getRestHighLevelClient().count(countRequest, RequestOptions.DEFAULT);
            return countResponse.getCount();
        } catch (IOException e) {
            log.error("exec elasticsearch count failed. request:{}, e:{}", countRequest, e);
            throw new RuntimeException(e);
        }
    }

    public Long count(Supplier<List<String>> indexSupplier, Supplier<QueryBuilder> builderSupplier) {
        return count(indexSupplier.get(), builderSupplier.get());
    }

    public void createIndex() {
        try {
            // judge index is exists
            String physicalIndex = physicalIndex();
            if (existsIndex(physicalIndex)) {
                log.info("{} index already existed!", physicalIndex);
                return;
            }

            // create index
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(physicalIndex);
            createIndexRequest.settings(getSettings());
            createIndexRequest.alias(new Alias(getAlias()));
            if (getMappingBuilder() != null) {
                createIndexRequest.mapping(getMappingBuilder());
            }
            restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            log.info("create index[{}] is success, shard:{}, replica:{}, maxResultWindow:{}", physicalIndex, getShards(), getReplicas(), getMaxResultWindow());
        } catch (ElasticsearchStatusException e) {
            if (e.getDetailedMessage().contains("resource_already_exists_exception")) {
                log.info("index ["+physicalIndex()+"] already exists.");
            } else {
                throw e;
            }
        } catch (Exception e) {
            log.error("create index ocuur error! ex:{}", e);
            throw new RuntimeException(e);
        }

    }

    public boolean existsIndex(String indexName) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean indexExists = false;
        try {
            indexExists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("judge index exists occur error! indexName:{}, ex:{}", indexName, e);
        }
        return indexExists;

    }

    public void deleteIndex(String indexName) {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        try {
            getRestHighLevelClient().indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("delete index occur error! indexName:{}, ex:{}", indexName, e);
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据指定别名获取该别名下所有的索引名，调用ES API
     * @param alias 别名
     * @return 返回索引名列表
     */
    public List<String> getIndicesByAlias(String alias) {
        GetAliasesRequest request = new GetAliasesRequest();
        request.aliases(alias);

        try {
            GetAliasesResponse response = restHighLevelClient.indices().getAlias(request, RequestOptions.DEFAULT);
            if (response == null) {
                return new ArrayList<>(0);
            }

            Map<String, Set<AliasMetaData>> aliases = response.getAliases();
            if (aliases != null) {
                return new ArrayList<>(aliases.keySet());
            }

        } catch (IOException e) {
            log.error("get indices by alias failed! alias:" + alias, e);
        }

        return new ArrayList<>(0);
    }

    public abstract String logicIndex();

    public String physicalIndex() {
        LocalDate now = LocalDate.now();
        return logicIndex() + UNDER_LINE + now.getYear() + StringUtils.frontFillStr(String.valueOf(now.getMonthValue()), "0", 2);
    }

    public int getShards() {
        return DEFAULT_SHARDS;
    }

    public int getReplicas() {
        return DEFAULT_REPLICAS;
    }

    public int getMaxResultWindow() {
        return MAX_RESULT_WINDOW;
    }

    public String getAlias() {
        return logicIndex();
    }

    public XContentBuilder getMappingBuilder() {
        return null;
    }

    public RestHighLevelClient getRestHighLevelClient() {
        return restHighLevelClient;
    }

    public Settings.Builder getSettings() {
        return Settings.builder()
                .put("index.number_of_shards", getShards())
                .put("index.number_of_replicas", getReplicas())
                .put("index.translog.durability", "async")
                .put("index.translog.sync_interval", "5s")
                .put("index.max_result_window", getMaxResultWindow());
    }
}
