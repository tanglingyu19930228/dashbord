package com.search.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.management.relation.Role;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@ApiModel("角色信息")
public class RoleEntity {
    @ApiModelProperty("角色id")
    private int roleId;

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty("父id")
    private Integer parentId;

    @ApiModelProperty("创建人id")
    private int createBy;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    @ApiModelProperty("创建时间格式=yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @ApiModelProperty("删除标志")
    private Integer delFlag;

    @ApiModelProperty("是否显示")
    private boolean show;


    private List<RoleEntity> roleEntityList;
}
