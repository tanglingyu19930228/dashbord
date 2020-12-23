package com.search.elasticsearch.automatic;

import lombok.Data;

/**
 * 描述：TODO
 * Author：linyue
 * Date：2020/4/13
 */
@Data
public class RedissonClientProperties {
    String host;
    int port;
    String password;
    int timeout;
}
