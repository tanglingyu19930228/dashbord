package com.search.bean;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Configuration
public class ElasticsearchConfig {

    @Value(value = "${elasticsearch.host}")
    private String host;

    @Value(value = "${elasticsearch.port}")
    private Integer port;

    private static final String HTTP = "http";

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        return new RestHighLevelClient(
            RestClient.builder(
                new HttpHost(host, port, HTTP)
            )
        );
    }

}
