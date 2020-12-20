package com.search.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.search.annotation.NoNeedLogin;
import com.search.common.controller.BaseController;
import com.search.common.page.PageDomain;
import com.search.common.utils.AESUtil;
import com.search.common.utils.GuavaCacheUtils;
import com.search.common.utils.R;
import com.search.common.utils.StringUtils;
import com.search.dao.SysUserDao;
import com.search.entity.RoleQueryReq;
import com.search.entity.SysUserEntity;
import com.search.entity.UserQueryReq;
import com.search.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.util.Arrays;


/**
 * 用户表
 *
 * @author Administrator
 * @date 2020-12-02 12:13:22
 */
@RestController
@RequestMapping(value = "/sysUser")
@Api(tags = "系统用户接口")
public class SysUserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    SysUserDao sysUserDao;

    /**
     * 登录
     * @param sysUserEntity 请求user对象
     * @return 返回结果
     */
    @PostMapping(value = "/login")
    @ApiOperation(value = "用户登录接口", tags = {"用户登录接口"})
    @NoNeedLogin
    public R login(@RequestBody @Valid SysUserEntity sysUserEntity,HttpServletResponse response) {
        logger.info("开始用户登录逻辑,请求参数={}", JSONObject.toJSONString(sysUserEntity));
        return sysUserService.login(sysUserEntity,response);
    }
    /**
     * 登录
     * @param sysUserEntity 请求user对象
     * @return 返回结果
     */
    @PostMapping(value = "/logout")
    @ApiOperation(value = "用户登出接口", tags = {"用户登出接口"})
    @NoNeedLogin
    public R logout(HttpServletRequest request) {
//        logger.info("开始用户登出辑,请求参数={}", JSONObject.toJSONString(sysUserEntity));
        return sysUserService.logout(request);
    }



    @PostMapping(value = "/resetPassword")
    @ApiOperation(value = "重置密码", tags = {"重置密码"})
    @NoNeedLogin
    public R resetPassword(@RequestBody @Valid SysUserEntity sysUserEntity,HttpServletResponse response) {
        logger.info("开始用户登录逻辑,请求参数={}", JSONObject.toJSONString(sysUserEntity));
        return sysUserService.resetPassword(sysUserEntity,response);
    }

    @RequestMapping(value = "/reset")
    @NoNeedLogin
    public void reset(String userName,String p,HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(userName)&&StringUtils.isNotBlank(p)){
                Object o = GuavaCacheUtils.cache.get("password:" + userName);
                if(o instanceof String){
                    if(AESUtil.decrypt(p, "1323232313232323").equals(String.class.cast(o))){
                        SysUserEntity result = sysUserDao.selectOneByUserName(userName);
                        result.setPassword(AESUtil.decrypt(p, "1323232313232323"));
                        int i = sysUserDao.updateByUserId(result);
                        PrintWriter writer = null;
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("content_type");
                        String responseMsg = "重设密码成功";
                        try {
                            writer = response.getWriter();
                            String json = JSON.toJSONString(responseMsg);
                            writer.write(json);
                            writer.flush();
                            writer.close();
                        } catch (Exception e1) {

                        }
                    }
                }
            }
            response.sendRedirect("/login");
        } catch (Exception e) {
            PrintWriter writer = null;
            response.setCharacterEncoding("UTF-8");
            response.setContentType("content_type");
            String responseMsg = "重设密码错误";
            try {
                writer = response.getWriter();
                String json = JSON.toJSONString(responseMsg);
                writer.write(json);
                writer.flush();
                writer.close();
            } catch (Exception e1) {

            }
        }
    }

    /**
     * 根据用户id或者userName查询该用户的角色信息
     */

    @PostMapping("/queryByUserNameOrId")
    @ApiOperation(value = "根据用户id或者userName查询该用户的角色信息", tags = {"根据用户id或者userName查询该用户的角色信息"})
    public R queryByUserNameOrId(@RequestBody @Valid UserQueryReq queryReq) {
        logger.info("根据用户id或者userName查询该用户的角色信息,请求参数={}", JSONObject.toJSONString(queryReq));
        return sysUserService.queryByUserNameOrId(queryReq);
    }

    /**
     * 根据角色id或者roleName查询该角色对应的用户信息
     */

    @PostMapping("/queryByRoleNameOrId")
    @ApiOperation(value = "根据角色id或者roleName查询该角色对应的用户信息", tags = {"根据角色id或者roleName查询该角色对应的用户信息"})
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
