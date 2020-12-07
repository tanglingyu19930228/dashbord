package com.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/11/28 12:11
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.search.dao"})
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }
}
