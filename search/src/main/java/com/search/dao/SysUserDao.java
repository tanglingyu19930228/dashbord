package com.search.dao;

import com.github.pagehelper.PageInfo;
import com.search.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表
 * 
 * @author Administrator
 * @date 2020-12-02 12:13:22
 */
@Mapper
public interface SysUserDao {

    SysUserEntity selectOneByUserName(@Param("userName") String userName);

    SysUserEntity getUserInfoByUserId(@Param("id") Integer id);

    UserRoleResp queryByUserNameOrId(@Param("queryReq") UserQueryReq queryReq);

    RoleUserResp queryByRoleNameOrId(@Param("queryReq") RoleQueryReq queryReq);

    int updateByUserId(@Param("sysUser") SysUserEntity sysUser);

    int deleteByUserIds(@Param("userIds") List<Integer> userIds);

    List<SysUserEntity> listByPage();

    int saveUser(SysUserEntity sysUserEntity);
}
