package com.search.service;

import com.github.pagehelper.PageInfo;
import com.search.entity.BizLogDto;
import com.search.common.utils.R;
import com.search.entity.RoleQueryReq;
import com.search.entity.UserQueryReq;
import com.search.entity.SysUserEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户表
 * @author Administrator
 */
public interface SysUserService  {

    R login(SysUserEntity sysUserEntity, HttpServletResponse response);

    SysUserEntity getUserInfoByUserId(Integer id);

    int saveUser(SysUserEntity sysUser);

    R queryByUserNameOrId(UserQueryReq queryReq);

    R queryByRoleNameOrId(RoleQueryReq queryReq);

    int updateByUserId(SysUserEntity sysUser);

    int deleteByUserIds(List<Integer> userIds);

    PageInfo<SysUserEntity> listByPage(int pageNum,int pageSize);

    R resetPassword(SysUserEntity sysUserEntity, HttpServletResponse response);

    R logout(HttpServletRequest request);

    void addUserOperationLog(BizLogDto operationLogDto);
}

