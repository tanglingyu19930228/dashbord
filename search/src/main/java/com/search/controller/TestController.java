package com.search.controller;

import cn.hutool.core.lang.Snowflake;
import com.search.biz.MailService;
import com.search.common.utils.R;
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
    @Autowired
    MailService mailService;

    @RequestMapping("/test")
    public String helloWorld() {
        final R a = mailService.sendSimpleMail("xuxiaoby@qq.com", "1", "a");
        final long l = snowflake.nextId();
        return "Hello World" + l;
    }
}
