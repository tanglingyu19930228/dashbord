package com.search.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("用户角色返回信息")
public class UserRoleResp {
    @ApiModelProperty(name = "用户id")
    private Integer id;
    /**
     * 用户名
     */
    @ApiModelProperty(name = "用户名")
    private String userName;
    /**
     * 手机号
     */
    @ApiModelProperty(name = "手机号")
    private String phone;
    /**
     * 姓名
     */
    @ApiModelProperty(name = "姓名")
    private String name;
    /**
     * 密码
     */
    @ApiModelProperty(name = "密码")
    private String password;
    /**
     * 是否删除
     */
    @ApiModelProperty(name = "是否删除")
    private Integer delFlag;
    /**
     * 是否超級管理
     */
    @ApiModelProperty(name = "是否超級管理")
    private Integer isAdmin;

    @ApiModelProperty(name = "角色信息集合")
    private List<RoleEntity> roleList;
}
