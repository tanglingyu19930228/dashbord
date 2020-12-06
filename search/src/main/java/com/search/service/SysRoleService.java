package com.search.service;

import com.github.pagehelper.PageInfo;
import com.search.common.page.PageDomain;
import com.search.entity.RoleEntity;

import java.util.List;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/12/6 19:19
 */
public interface SysRoleService {

    List<RoleEntity> queryRoleByLike(RoleEntity roleEntity);

    PageInfo<RoleEntity> queryRolePage(PageDomain pageDomain);

    int updateRole(RoleEntity roleEntity);

    int batchDeleteRole(Integer[] roleIds);
}
