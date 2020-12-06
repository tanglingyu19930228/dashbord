package com.search.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.search.common.page.PageDomain;
import com.search.dao.SysRoleDao;
import com.search.entity.RoleEntity;
import com.search.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/12/6 19:20
 */
@Service
@Slf4j
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleDao sysRoleDao;

    @Override
    public List<RoleEntity> queryRoleByLike(RoleEntity roleEntity) {
        return sysRoleDao.queryRoleByLike(roleEntity);
    }

    @Override
    public PageInfo<RoleEntity> queryRolePage(PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize());
        List<RoleEntity> roleEntities = sysRoleDao.queryRolePage();
        return CollectionUtils.isNotEmpty(roleEntities) ? new PageInfo<RoleEntity>(roleEntities) : new PageInfo<RoleEntity>(Collections.emptyList());
    }

    @Override
    public int updateRole(RoleEntity roleEntity) {
        return sysRoleDao.updateRole(roleEntity);
    }

    @Override
    public int batchDeleteRole(Integer[] roleIds) {
        return sysRoleDao.batchDeleteRole(roleIds);
    }
}
