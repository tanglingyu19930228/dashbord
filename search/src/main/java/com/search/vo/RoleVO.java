package com.search.vo;

import com.search.entity.RoleEntity;
import com.search.entity.SysRoleEntity;
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
public class RoleVO {

    /** 当前需要创建权限的用户 */
    private Integer userId;

    private List<SysRoleEntity> sysRoleEntityList;

}
