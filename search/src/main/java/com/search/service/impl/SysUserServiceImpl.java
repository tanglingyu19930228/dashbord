package com.search.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.search.dao.SysUserDao;
import com.search.entity.SysUserEntity;
import com.search.service.SysUserService;


/**
 * @author Administrator
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

}