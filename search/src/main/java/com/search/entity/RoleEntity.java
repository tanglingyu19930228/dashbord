package com.search.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RoleEntity {
    private int roleId;
    private String name;
    private int createBy;
    private Date createDate;
    private boolean delFlag;
}
