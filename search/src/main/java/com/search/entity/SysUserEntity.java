package com.search.entity;


import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户表
 * 
 * @author Administrator
 * @date 2020-12-02 12:13:22
 */
@Data
@ApiModel(description = "用户实体")
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty(value = "用户id")
	private Integer id;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String userName;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String phone;
	/**
	 * 姓名
	 */
	@ApiModelProperty(value = "姓名")
	private String name;
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String password;
	/**
	 * 是否删除
	 */
	@ApiModelProperty(value = "是否删除")
	private Integer delFlag;
	/**
	 * 是否超級管理
	 */
	@ApiModelProperty(value = "是否超級管理")
	private Integer isAdmin;

}
