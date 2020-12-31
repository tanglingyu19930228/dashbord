package com.search.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色表
 * 
 * @author Administrator
 * @date 2020-12-03 18:40:58
 */
@Data
@ApiModel("系统角色实体")
public class SysRoleEntity implements Serializable {

	/**
	 * 
	 */
	@ApiModelProperty("角色id")
	private Integer id;
	/**
	 * 
	 */
	@ApiModelProperty("角色名")
	private String name;
	/**
	 * 
	 */
	@ApiModelProperty("创建人id")
	private Integer createBy;
	/** 父id*/
	@ApiModelProperty("父id")
	private Integer parentId;
	/**
	 * 
	 */
	@ApiModelProperty("创建日期")
	private Date createDate;
	/**
	 * 是否禁用
	 */
	@ApiModelProperty("是否禁用")
	private Integer delFlag;

	/** 是否展示 */
	@ApiModelProperty("是否展示")
	private boolean show;

	private Integer titleId;

	@ApiModelProperty("子角色集合")
	private List<SysRoleEntity> list;

}
