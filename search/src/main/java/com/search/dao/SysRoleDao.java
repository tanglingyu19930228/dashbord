package com.search.dao;

import com.search.entity.RoleEntity;
import com.search.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/12/6 19:24
 */
@Mapper
public interface SysRoleDao {
    List<RoleEntity> queryRoleByLike(@Param("roleEntity") RoleEntity roleEntity);

    List<RoleEntity> queryRolePage();

    int updateRole(@Param("roleEntity") RoleEntity roleEntity);

    int batchDeleteRole(@Param("roleIds") Integer[] roleIds);

    List<SysRoleEntity> selectRoleListByUser(SysRoleEntity sysRoleEntity);

    /**
     * 查询SysRoleEntity
     *
     * @param sysRoleEntity
     * @return SysRoleEntity
     */
    public SysRoleEntity selectSysRoleOne(SysRoleEntity sysRoleEntity);

    /**
     * 查询SysRoleEntity列表
     *
     * @param sysRoleEntity
     * @return SysRoleEntity 集合
     */
    public List<SysRoleEntity> selectSysRoleList(SysRoleEntity sysRoleEntity);

    /**
     * 新增SysRoleEntity
     *
     * @param sysRoleEntity
     * @return 结果
     */
    public int insertSysRole(List<SysRoleEntity> list);

    /**
     * 修改sysRole
     *
     * @param sysRoleEntity
     * @return 结果
     */
    public int updateSysRole(List<SysRoleEntity> list);
}
