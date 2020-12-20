package com.search.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@ApiModel("角色用户返回")
public class RoleUserResp {
    @ApiModelProperty(name = "角色id")
    private int id;

    @ApiModelProperty(name = "角色名")
    private String name;

    @ApiModelProperty(name = "创建者id")
    private int createBy;

    @ApiModelProperty(name = "父id")
    private Integer parentId;

    @ApiModelProperty(name = "创建时间")
    private Date createDate;

    @ApiModelProperty(name = "是否删除")
    private boolean delFlag;

    @ApiModelProperty(name = "用户集合")
    private List<SysUserEntity> userList;

}
