package com.search.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 角色-用户表
 * 
 * @author Administrator
 * @date 2020-12-03 18:40:58
 */
@Data
@TableName("sys_role_user")
public class SysRoleUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private Integer userId;
	/**
	 * 
	 */
	private Integer roleId;
	/**
	 * 
	 */
	private Integer delFlag;

}
