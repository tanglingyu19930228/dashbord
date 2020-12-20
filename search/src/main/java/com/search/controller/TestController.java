package com.search.controller;

import cn.hutool.core.lang.Snowflake;
import com.search.biz.MailService;
import com.search.common.utils.R;
import com.search.entity.Order;
import com.search.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/11/28 12:27
 */
@RestController("/test")
@ApiIgnore
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

    @Resource
    private OrderService orderService;

    @RequestMapping("/testSphere")
    public String testSphere() {
        for (int i = 0; i < 100; i++) {
            Order order = new Order();
            order.setName("电脑" + i);
            order.setType("办公");
            orderService.save(order);
        }
        return "Test Sphere";
    }

    @RequestMapping("/testGetById")
    public void testGetById() {
        long id = 543931333139759104L;
        Order order = orderService.getById(id);
        System.out.println(order.toString());
    }
}
