package com.search.config;

import cn.hutool.core.lang.Snowflake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Configuration
public class SnowFlakeConfig {

    @Value(value = "${workId}")
    private Long workId;

    @Value(value = "${dataCenter}")
    private Long dataCenter;

    @Bean
    public Snowflake snowflake(){
        return new Snowflake(workId,dataCenter);
    }

}
