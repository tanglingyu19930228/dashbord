package com.search.service;

import com.github.pagehelper.PageInfo;
import com.search.common.page.PageDomain;
import com.search.common.utils.R;
import com.search.entity.RoleQueryReq;
import com.search.entity.UserQueryReq;
import com.search.entity.SysUserEntity;

import java.util.List;

/**
 * 用户表
 * @author Administrator
 */
public interface SysUserService  {

    R login(SysUserEntity sysUserEntity);

    SysUserEntity getUserInfoByUserId(Integer id);

    int saveUser(SysUserEntity sysUser);

    R queryByUserNameOrId(UserQueryReq queryReq);

    R queryByRoleNameOrId(RoleQueryReq queryReq);

    int updateByUserId(SysUserEntity sysUser);

    int deleteByUserIds(List<Integer> userIds);

    PageInfo<SysUserEntity> listByPage(int pageNum,int pageSize);
}

