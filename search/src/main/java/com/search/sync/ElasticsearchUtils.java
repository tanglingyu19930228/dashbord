package com.search.sync;

import com.alibaba.fastjson.JSON;
import com.search.entity.SysArticleEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
