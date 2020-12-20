package com.search.entity;


import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 角色-用户表
 * 
 * @author Administrator
 * @date 2020-12-03 18:40:58
 */
@Data
@ApiModel("角色用户实体")
public class SysRoleUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
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
