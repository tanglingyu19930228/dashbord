package com.search.dao;


import com.search.entity.Order;
import org.apache.ibatis.annotations.Param;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/12/10 22:23
 */
public interface OrderMapper  {
  int save(@Param("entity") Order entity);
}
