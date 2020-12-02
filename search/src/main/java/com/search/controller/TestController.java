package com.search.controller;

import cn.hutool.core.lang.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/11/28 12:27
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    Snowflake snowflake;

    @RequestMapping("/test")
    public String helloWorld() {
        final long l = snowflake.nextId();
        return "Hello World" + l;
    }
}
