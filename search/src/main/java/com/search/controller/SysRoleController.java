package com.search.controller;

import com.github.pagehelper.PageInfo;
import com.search.annotation.BizLog;
import com.search.biz.RoleService;
import com.search.common.controller.BaseController;
import com.search.common.page.PageDomain;
import com.search.common.utils.R;
import com.search.common.utils.StringUtils;
import com.search.entity.RoleEntity;
import com.search.service.SysRoleService;
import com.search.vo.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/12/6 19:18
 */

@RestController
@RequestMapping(value = "/sysRole")
@Api(tags = "用户权限接口")
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService sysRoleService;

    @Autowired
    RoleService roleService;

    @PostMapping(value = "/addUserRole")
    @ApiOperation(value = "增加权限接口", tags = {"增加权限接口"})
    @BizLog(action = "增加权限接口")
    public R addUserRole(@RequestBody @ApiParam(value = "用户角色实体") RoleVO roleVO){
        if(Objects.isNull(roleVO)){
            return R.error("请求参数为空");
        }
        if(StringUtils.isBlank(roleVO.getUserName())||StringUtils.isBlank(roleVO.getPassword())){
            return R.error("用户名密码不能为空");
        }
        return roleService.addUserRole(roleVO);
    }

    @ApiOperation(value = "获取权限类别", tags = {"获取权限类别"})
    @PostMapping(value = "/getRoleList")
    @BizLog(action = "获取权限类别")
    public R selectRoleList(@RequestBody @ApiParam(value = "用户角色实体") RoleVO roleVO){
        return roleService.selectRoleList(roleVO);
    }

    /**
     * 角色信息模糊查询
     */
    @PostMapping("/queryRoleByLike")
    @BizLog(action = "角色信息模糊查询")
    @ApiOperation(value = "角色信息模糊查询")
    public R queryRoleByLike(@RequestBody @Valid RoleEntity roleEntity) {
        List<RoleEntity> roleEntities = sysRoleService.queryRoleByLike(roleEntity);
        return R.ok(roleEntities);
    }

    /**
     * 角色信息分页查询
     */
    @PostMapping("/queryRolePage")
    @BizLog(action = "角色信息分页查询")
    @ApiOperation(value = "角色信息分页查询")
    public R queryRolePage(@RequestBody @Valid PageDomain pageDomain) {
        PageInfo<RoleEntity> roleEntityPageInfo = sysRoleService.queryRolePage(pageDomain);
        return R.ok(roleEntityPageInfo);
    }

    /**
     * 更新角色信息
     */
    @PostMapping("/updateRole")
    @BizLog(action = "更新角色信息")
    @ApiOperation(value = "更新角色信息")
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
    @BizLog(action = " 批量删除角色")
    @ApiOperation(value = "批量删除角色")
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
