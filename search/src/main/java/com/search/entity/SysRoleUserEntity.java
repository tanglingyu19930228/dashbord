package com.search.entity;


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
