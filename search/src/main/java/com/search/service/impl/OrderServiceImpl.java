package com.search.service.impl;

import com.search.dao.OrderMapper;
import com.search.entity.Order;
import com.search.service.OrderService;
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
}
