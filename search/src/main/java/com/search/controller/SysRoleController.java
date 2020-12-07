package com.search.controller;

import com.github.pagehelper.PageInfo;
import com.search.common.page.PageDomain;
import com.search.common.utils.R;
import com.search.entity.RoleEntity;
import com.search.service.SysRoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/12/6 19:18
 */

@RestController("/sysRole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;
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

    /**
     * 角色信息模糊查询
     */
    @PostMapping("/queryRoleByLike")
    public R queryRoleByLike(@RequestBody @Valid RoleEntity roleEntity) {
        List<RoleEntity> roleEntities = sysRoleService.queryRoleByLike(roleEntity);
        return R.ok(roleEntities);
    }

    /**
     * 角色信息分页查询
     */
    @PostMapping("/queryRolePage")
    public R queryRolePage(@RequestBody @Valid PageDomain pageDomain) {
        PageInfo<RoleEntity> roleEntityPageInfo = sysRoleService.queryRolePage(pageDomain);
        return R.ok(roleEntityPageInfo);
    }

    /**
     * 更新角色信息
     */
    @PostMapping("/updateRole")
    public R updateRole(@RequestBody @Valid RoleEntity roleEntity) {
        int result = sysRoleService.updateRole(roleEntity);
        if (result == 1) {
            return R.ok();
        }
        return R.error("系统异常");
    }

    /**
     * 批量删除角色
     */
    @PostMapping("/batchDeleteRole")
    public R batchDeleteRole(@RequestBody @Valid Integer[] roleIds) {
        int result = sysRoleService.batchDeleteRole(roleIds);
        if (result == roleIds.length) {
            return R.ok();
        }
        if (result == 0) {
            return R.error("删除失败");
        }
        return R.error("部分删除成功,部分删除失败");
    }
}
