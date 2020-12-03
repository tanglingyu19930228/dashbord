package com.search.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.search.common.utils.PageUtils;
import com.search.common.utils.R;
import com.search.entity.SysUserEntity;

import java.util.Map;

/**
 * 用户表
 * @author Administrator
 */
public interface SysUserService extends IService<SysUserEntity> {

    R login(SysUserEntity sysUserEntity);
}

