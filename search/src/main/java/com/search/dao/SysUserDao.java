package com.search.dao;

import com.search.entity.QueryReq;
import com.search.entity.SysUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.search.entity.UserRoleResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表
 * 
 * @author Administrator
 * @date 2020-12-02 12:13:22
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

    SysUserEntity selectOneByUserName(@Param("userName") String userName);

    SysUserEntity getUserInfoByUserId(@Param("id") Integer id);

    UserRoleResp queryByUserNameOrId(@Param("queryReq") QueryReq queryReq);
}
