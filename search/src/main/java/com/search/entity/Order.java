package com.search.entity;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/12/10 21:48
 */

@Data
public class Order {
    private Long id;
    private String name;
    private String type;
    private Date gmtCreate;
}
