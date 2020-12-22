package com.search.sync;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.search.common.utils.R;
import com.search.common.utils.StringUtils;
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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class ElasticsearchUtils {

    private static final String INDEX_NAME = "newindex";

    @Autowired
    RestHighLevelClient restHighLevelClient;

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

    public Map<String,Object> getSysArticleDataByCondition(String indexName,SysArticleEntity sysArticleEntity){

        return null;
    }

    public static void main(String[] args) {
        final String[] split = "asdf.fff".split("\\.");
        Arrays.stream(split).forEach(System.out::println);
        System.out.println("asdf.fff".substring(0,"asdf.fff".indexOf(".")));
    }


//    public static void main(String[] args) {
//        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(SysArticleEntity.class);
//        List<String> collect = Arrays.stream(propertyDescriptors).map(PropertyDescriptor::getName).collect(Collectors.toList());
//        collect.forEach(System.out::println);
//    }

    private static String[] getPropertiesNameByClass(Class<?> clazz){
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            return Arrays.stream(propertyDescriptors).map(PropertyDescriptor::getName).collect(Collectors.toList()).toArray(new String[]{});
        } catch (IntrospectionException e) {
            log.error("自省失败咯！！！",e);
            return null;
        }
    }

//    public static void main(String[] args) {
//        final String[] propertiesNameByClass = getPropertiesNameByClass(SysArticleEntity.class);
//        Arrays.stream(propertiesNameByClass).forEach(System.out::println);
//    }

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

    public R doSearch(QueryVO queryVO,String indexName){
        if(Objects.isNull(queryVO)){
            return R.error("查询失败");
        }
        if(Objects.isNull(queryVO.getTitleId())){
            return R.error("未知的品牌产品查询");
        }
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(StringUtils.isBlank(indexName)?INDEX_NAME:indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.matchQuery("titleId",queryVO.getTitleId()));
        if(!(CollectionUtils.isEmpty(queryVO.getEmotionList())||queryVO.getEmotionList().size()==3)){
            List<Integer> emotionList = queryVO.getEmotionList();
            boolQuery.filter(QueryBuilders.multiMatchQuery("emotionType","1","2"));
        }

        return R.error();
    }

}
