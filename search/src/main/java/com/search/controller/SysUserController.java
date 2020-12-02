package com.search.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.search.entity.SysUserEntity;
import com.search.service.SysUserService;
import com.search.common.utils.PageUtils;
import com.search.common.utils.R;



/**
 * 用户表
 *
 * @author Administrator
 * @date 2020-12-02 12:13:22
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping(value = "/login")
    public R login(@RequestBody SysUserEntity sysUserEntity){
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){

        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		SysUserEntity sysUser = sysUserService.getById(id);
        return R.ok(sysUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SysUserEntity sysUser){
		sysUserService.save(sysUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SysUserEntity sysUser){
		sysUserService.updateById(sysUser);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @Deprecated
    public R delete(@RequestBody Integer[] ids){
		sysUserService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
