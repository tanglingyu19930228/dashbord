package com.search.vo;

import com.search.entity.RoleEntity;
import com.search.entity.SysRoleEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Administrator
 */
@Getter
@Setter
@ToString
@ApiModel(description = "用户角色")
public class RoleVO {

    /** 当前需要创建权限的用户 */
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    private String userName;

    private String password;

    @ApiModelProperty(value = "传入的权限")
    private List<SysRoleEntity> sysRoleEntityList;

}
