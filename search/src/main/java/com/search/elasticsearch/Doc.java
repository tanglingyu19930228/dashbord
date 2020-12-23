package com.search.elasticsearch;

import lombok.Data;

/**
 * Elasticsearch中存储文档对象的父类, 用于封装id字段
 * @author liujianguo
 */
@Data
public class Doc {
    private String id;
}
