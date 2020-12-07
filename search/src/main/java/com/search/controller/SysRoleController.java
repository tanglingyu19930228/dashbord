package com.search.controller;


import com.search.biz.RoleService;
import com.search.common.utils.R;
import com.search.entity.RoleEntity;
import com.search.entity.UserRoleResp;
import com.search.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/role")
public class SysRoleController {


    @Autowired
    RoleService roleService;


    @PostMapping(value = "/addUserRole")
    public R addUserRole(@RequestBody RoleVO roleVO){

        return roleService.addUserRole(roleVO);
    }

    @PostMapping(value = "/getRoleList")
    public R selectRoleList(@RequestBody RoleVO roleVO){
        return roleService.selectRoleList(roleVO);
    }

}
