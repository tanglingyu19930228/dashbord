package com.search.dao;

import com.search.entity.SysRoleUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色-用户表
 * @author Administrator
 * @date 2020-12-03 18:40:58
 */
@Mapper
public interface SysRoleUserDao {

    int save(SysRoleUserEntity sysRoleUserEntity);

    int update(SysRoleUserEntity sysRoleUserEntity);

    List<SysRoleUserEntity> selectList(SysRoleUserEntity sysRoleUserEntity);
}
