package com.search.dao;

import com.search.entity.SysRoleUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色-用户表
 * @author Administrator
 * @date 2020-12-03 18:40:58
 */
@Mapper
public interface SysRoleUserDao extends BaseMapper<SysRoleUserEntity> {
	
}
