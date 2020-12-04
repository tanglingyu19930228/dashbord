package com.search.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class UserRoleResp {
    private Integer id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 姓名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否删除
     */
    private Integer delFlag;
    /**
     * 是否超級管理
     */
    private Integer isAdmin;

    private List<RoleEntity> roleList;
}
