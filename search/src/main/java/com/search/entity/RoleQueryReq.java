package com.search.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("角色查询请求入参")
public class RoleQueryReq {


    //用户名
    @ApiModelProperty(name="用户名")
    private String roleName;

    //角色id
    @ApiModelProperty(name="角色id")
    private int roleId;
}
