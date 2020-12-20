package com.search.service.impl;

import com.search.dao.OrderMapper;
import com.search.entity.Order;
import com.search.service.OrderService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/12/10 22:22
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Override
    public int save(Order entity) {
        return orderMapper.save(entity);
    }

    @Override
    @Cacheable(cacheNames = "TangLingYu", key = "#id")
    public Order getById(long id) {
        System.out.println("查询没走缓存,查询id=" + id + "");
        Order order = orderMapper.getById(id);
        return order;
    }
}
