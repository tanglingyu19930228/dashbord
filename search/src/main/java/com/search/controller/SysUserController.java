package com.search.controller;

import java.util.Arrays;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.search.common.controller.BaseController;
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

    @PostMapping(value = "/login")
    public R login(@RequestBody @Valid SysUserEntity sysUserEntity) {
        logger.info("开始用户登录逻辑,请求参数={}", JSONObject.toJSONString(sysUserEntity));
        return sysUserService.login(sysUserEntity);
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
        SysUserEntity sysUser = sysUserService.getById(id);
        if (sysUser == null) {
            return R.ok("信息不存在");
        }
        return R.ok(sysUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SysUserEntity sysUser) {
        sysUserService.save(sysUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SysUserEntity sysUser) {
        sysUserService.updateById(sysUser);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @Deprecated
    public R delete(@RequestBody Integer[] ids) {
        boolean result = sysUserService.removeByIds(Arrays.asList(ids));
        if(result){
            return R.ok();
        }
        return R.error("删除失败");
    }

}
