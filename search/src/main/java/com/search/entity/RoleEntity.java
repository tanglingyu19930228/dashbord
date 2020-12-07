package com.search.entity;

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
    private Date createDate;
    private boolean delFlag;

    private List<RoleEntity> roleEntityList;
}
