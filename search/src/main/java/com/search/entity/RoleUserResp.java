package com.search.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RoleUserResp {
    private int id;
    private String name;
    private int createBy;
    private Date createDate;
    private boolean delFlag;
    private List<SysUserEntity> userList;

}
