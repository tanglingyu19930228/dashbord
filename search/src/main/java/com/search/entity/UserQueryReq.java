package com.search.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户查询实体")
public class UserQueryReq {

    //用户名
    @ApiModelProperty(name = "用户名")
    private String userName;

    //用户id
    @ApiModelProperty(name = "用户id")
    private int userId;

}
