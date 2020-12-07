package com.search.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.management.relation.Role;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class RoleEntity {
    private int roleId;
    private String name;
    private Integer parentId;
    private int createBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date createDate;
    private boolean delFlag;

    private List<RoleEntity> roleEntityList;
}
