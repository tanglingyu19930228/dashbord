package com.search.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.search.common.utils.R;
import com.search.entity.RoleQueryReq;
import com.search.entity.UserQueryReq;
import com.search.entity.SysUserEntity;

/**
 * 用户表
 * @author Administrator
 */
public interface SysUserService extends IService<SysUserEntity> {

    R login(SysUserEntity sysUserEntity);

    SysUserEntity getUserInfoByUserId(Integer id);

    int saveUser(SysUserEntity sysUser);

    R queryByUserNameOrId(UserQueryReq queryReq);

    R queryByRoleNameOrId(RoleQueryReq queryReq);
}

