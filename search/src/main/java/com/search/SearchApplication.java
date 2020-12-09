package com.search;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/11/28 12:11
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.search.dao"})
public class SearchApplication {
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        //可以通过环境变量获取你的mapper路径,这样mapper扫描可以通过配置文件配置了
        scannerConfigurer.setBasePackage("com.search.dao");
        return scannerConfigurer;
    }
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }
}
