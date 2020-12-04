package com.search.controller;

import java.util.Arrays;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.search.common.controller.BaseController;
import com.search.entity.RoleQueryReq;
import com.search.entity.UserQueryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.search.entity.SysUserEntity;
import com.search.service.SysUserService;
import com.search.common.utils.R;

import javax.validation.Valid;


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
    public R queryByUserNameOrId(@RequestBody @Valid UserQueryReq queryReq){
        logger.info("根据用户id或者userName查询该用户的角色信息,请求参数={}", JSONObject.toJSONString(queryReq));
        return sysUserService.queryByUserNameOrId(queryReq);
    }

    /**
     * 根据角色id或者roleName查询该角色对应的用户信息
     */

    @PostMapping("/queryByRoleNameOrId")
    public R queryByRoleNameOrId(@RequestBody @Valid RoleQueryReq queryReq){
        logger.info("根据角色id或者roleName查询该用户的角色信息,请求参数={}", JSONObject.toJSONString(queryReq));
        return sysUserService.queryByRoleNameOrId(queryReq);
    }




    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam(value = "params") @Valid Map<String, Object> params) {
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") @Valid Integer id) {
        logger.info("查询用户详细信息,用户id={}",id);
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
    public R save(@RequestBody @Valid SysUserEntity sysUser) {
        int result=sysUserService.saveUser(sysUser);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody @Valid SysUserEntity sysUser) {
        sysUserService.updateById(sysUser);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @Deprecated
    public R delete(@RequestBody @Valid Integer[] ids) {
        boolean result = sysUserService.removeByIds(Arrays.asList(ids));
        if (result) {
            return R.ok();
        }
        return R.error("删除失败");
    }

}
