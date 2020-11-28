package com.search.controller;

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

    @RequestMapping("/test")
    public String helloWorld() {
        return "Hello World";
    }
}
