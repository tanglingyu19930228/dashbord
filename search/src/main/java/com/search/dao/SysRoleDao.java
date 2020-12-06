package com.search.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.search.entity.RoleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/12/6 19:24
 */
public interface SysRoleDao  extends BaseMapper<RoleEntity> {
    List<RoleEntity> queryRoleByLike(@Param("roleEntity") RoleEntity roleEntity);

    List<RoleEntity> queryRolePage();

    int updateRole(@Param("roleEntity") RoleEntity roleEntity);

    int batchDeleteRole(@Param("roleIds") Integer[] roleIds);
}
