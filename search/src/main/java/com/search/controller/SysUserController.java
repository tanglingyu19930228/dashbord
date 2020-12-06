package com.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.search.common.controller.BaseController;
import com.search.common.page.PageDomain;
import com.search.common.utils.R;
import com.search.entity.RoleQueryReq;
import com.search.entity.SysUserEntity;
import com.search.entity.UserQueryReq;
import com.search.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;


/**
 * 用户表
 *
 * @author Administrator
 * @date 2020-12-02 12:13:22
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录
     *
     * @param sysUserEntity
     * @return
     */
    @PostMapping(value = "/login")
    public R login(@RequestBody @Valid SysUserEntity sysUserEntity) {
        logger.info("开始用户登录逻辑,请求参数={}", JSONObject.toJSONString(sysUserEntity));
        return sysUserService.login(sysUserEntity);
    }

    /**
     * 根据用户id或者userName查询该用户的角色信息
     */

    @PostMapping("/queryByUserNameOrId")
    public R queryByUserNameOrId(@RequestBody @Valid UserQueryReq queryReq) {
        logger.info("根据用户id或者userName查询该用户的角色信息,请求参数={}", JSONObject.toJSONString(queryReq));
        return sysUserService.queryByUserNameOrId(queryReq);
    }

    /**
     * 根据角色id或者roleName查询该角色对应的用户信息
     */

    @PostMapping("/queryByRoleNameOrId")
    public R queryByRoleNameOrId(@RequestBody @Valid RoleQueryReq queryReq) {
        logger.info("根据角色id或者roleName查询该用户的角色信息,请求参数={}", JSONObject.toJSONString(queryReq));
        return sysUserService.queryByRoleNameOrId(queryReq);
    }


    /**
     * 列表
     */
    @PostMapping("/list")
    public R list(@RequestBody @Valid PageDomain pageDomain) {
        //分页查询用户列表
        logger.info("分页查询用户列表");
        PageInfo<SysUserEntity> sysUserEntityPageInfo = sysUserService.listByPage(pageDomain.getPageNum(), pageDomain.getPageSize());
        return R.ok(sysUserEntityPageInfo);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") @Valid Integer id) {
        logger.info("查询用户详细信息,用户id={}", id);
        SysUserEntity sysUser = sysUserService.getUserInfoByUserId(id);
        if (sysUser == null) {
            return R.ok("信息不存在");
        }
        return R.ok(sysUser);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    @Deprecated
    public R save(@RequestBody @Valid SysUserEntity sysUser) {
        int result = sysUserService.saveUser(sysUser);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody @Valid SysUserEntity sysUser) {
        logger.info("更新用户信息,更新信息sysUser={}", JSONObject.toJSONString(sysUser));
        int result = sysUserService.updateByUserId(sysUser);
        return result == 1 ? R.ok() : R.error("系统异常");
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody @Valid Integer[] ids) {
        int result = sysUserService.deleteByUserIds(Arrays.asList(ids));
        if (result == ids.length) {
            return R.ok();
        }
        if (result == 0) {
            return R.error("删除失败");
        }
        return R.error("部分删除成功,部分删除失败");
    }
}
