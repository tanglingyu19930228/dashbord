package com.search.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class RoleUserResp {
    private int id;
    private String name;
    private int createBy;
    private Integer parentId;
    private Date createDate;
    private boolean delFlag;
    private List<SysUserEntity> userList;

}
