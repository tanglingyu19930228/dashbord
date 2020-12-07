package com.search.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 角色表
 * 
 * @author Administrator
 * @date 2020-12-03 18:40:58
 */
@Data
@TableName("sys_role")
public class SysRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private Integer createBy;
	/** 父id*/
	private Integer parentId;
	/**
	 * 
	 */
	private Date createDate;
	/**
	 * 是否禁用
	 */
	private Integer delFlag;

	/** 是否展示 */
	private boolean show;

	private List<SysRoleEntity> list;

}
