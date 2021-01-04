package com.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.search.entity.BizLogDto;
import com.search.bean.SpringConfiguration;
import com.search.biz.MailService;
import com.search.common.utils.*;
import com.search.config.WebFilter;
import com.search.dao.BizLogDao;
import com.search.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.search.dao.SysUserDao;
import com.search.service.SysUserService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.WebUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;


/**
 * @author Administrator
 */
@Service("sysUserService")
@Slf4j
public class SysUserServiceImpl implements SysUserService {


    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private SpringConfiguration springConfiguration;

    @Autowired
    MailService mailService;

    @Resource
    private BizLogDao bizLogDao;

    private String REGEXP = null;

    @PostConstruct
    public void initParameters() {
        REGEXP = springConfiguration.getEmail();
    }


    @Override
    public R login(SysUserEntity sysUserEntity, HttpServletResponse response) {
        String userName = sysUserEntity.getUserName();
        if (StringUtils.isBlank(userName)) {
            log.info("用户名不能为空");
            return R.error("用户名不能为空,请输入用户名");
        }
        //校验邮箱
        R r = checkEmail(userName);
        if (!r.isSuccess()) {
            return r;
        }
        //根据用户名查用户
        SysUserEntity result = sysUserDao.selectOneByUserName(userName);
        if (result == null) {
            log.error("用户名不存在:userName={}", sysUserEntity.getUserName());
            return R.error("用户名不存在,请输入正确的用户名");
        }
        if ("1".equals(result.getDelFlag())) {
            return R.error("该用户已经被删除");
        }
        //校验password
        String password = result.getPassword();
        String webPassword = sysUserEntity.getPassword();
        if (StringUtils.isEmpty(webPassword)) {
            return R.error("用户密码不能为空");
        }
        if (password.equals(sysUserEntity.getPassword())) {
            //缓存用户token信息
            String token = UuidUtil.generateUuid();
            GuavaCacheUtils.cache.put(String.format("LOGIN_TOKEN_%s", token), sysUserEntity);
            //设置cookie
            Cookie cookie = new Cookie("login_token", token);
            cookie.setSecure(false);
            cookie.setPath("/");
            //先设置成5天,反正login_token由后端控制
            cookie.setMaxAge(60 * 60 * 120);
            response.addCookie(cookie);
            RequestContextHolder.getRequestAttributes().setAttribute(WebFilter.KEY, result, RequestAttributes.SCOPE_REQUEST);
            return R.ok("登录成功").setUserId(Long.valueOf(result.getId())).setToken(token);
        }
        return R.error("密码错误,请输入正确的密码");

    }

    @Override
    public SysUserEntity getUserInfoByUserId(Integer id) {
        return sysUserDao.getUserInfoByUserId(id);
    }

    /**
     * 用户入库
     */
    @Override
    public int saveUser(SysUserEntity sysUser) {
        return 0;
    }


    @Override
    public R queryByUserNameOrId(UserQueryReq queryReq) {
        try {
            UserRoleResp resp = sysUserDao.queryByUserNameOrId(queryReq);
            if (Objects.isNull(resp)) {
                log.error("查询权限无数据");
                return R.error("查询无数据");
            }
            List<RoleEntity> roleList = resp.getRoleList();
            if (Objects.isNull(roleList)) {
                return R.error("查询无数据");
            }
            resp.setRoleList(convertTree(roleList, -1));
            return R.ok(resp);
        } catch (Exception e) {
            log.error("查询异常");
            return R.error("服务器异常");
        }
    }

    /**
     * 此处 -1 表示根 即 品牌 产品 其他
     *
     * @param roleList 权限集合
     * @param parentId 父id
     * @return 递归返回树
     */
    private List<RoleEntity> convertTree(List<RoleEntity> roleList, Integer parentId) {
        List<RoleEntity> result = new ArrayList<>();
        List<RoleEntity> temp = new ArrayList<>();
        for (RoleEntity one : roleList) {
            if (one.getParentId().equals(parentId)) {
                temp = convertTree(roleList, one.getRoleId());
                if (!temp.isEmpty()) {
                    one.setRoleEntityList(temp);
                }
                result.add(one);
            }

        }
        return result;
    }


    @Override
    public R queryByRoleNameOrId(RoleQueryReq queryReq) {
        RoleUserResp resp = sysUserDao.queryByRoleNameOrId(queryReq);
        return R.ok(resp);
    }

    @Override
    public int updateByUserId(SysUserEntity sysUser) {
        return sysUserDao.updateByUserId(sysUser);
    }

    @Override
    public int deleteByUserIds(List<Integer> userIds) {
        return sysUserDao.deleteByUserIds(userIds);
    }

    @Override
    public PageInfo<SysUserEntity> listByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUserEntity> sysUserEntities = sysUserDao.listByPage();
        if (CollectionUtils.isNotEmpty(sysUserEntities)) {
            return new PageInfo<>(sysUserEntities);
        }
        return new PageInfo<>(Collections.emptyList());
    }

    @Override
    public R resetPassword(SysUserEntity sysUserEntity, HttpServletResponse response) {
        if (Objects.isNull(sysUserEntity)) {
            return R.error("请求为空");
        }
        if (StringUtils.isBlank(sysUserEntity.getUserName())) {
            return R.error("未知用户请求");
        }
        if (StringUtils.isBlank(sysUserEntity.getPassword()) || StringUtils.isBlank(sysUserEntity.getNewPassword())) {
            return R.error("请输入两次密码");
        }
        if (!sysUserEntity.getPassword().equals(sysUserEntity.getNewPassword())) {
            return R.error("两次密码输入不一致");
        }
        try {
            String encrypt = AESUtil.encrypt(sysUserEntity.getNewPassword(), "1323232313232323");
            GuavaCacheUtils.cache.put("password:" + sysUserEntity.getUserName(), sysUserEntity.getNewPassword());
            mailService.sendSimpleMail(sysUserEntity.getUserName(), "新的验证码", "http://47.115.160.133:19999/sysUser/reset?userName=" + sysUserEntity.getUserName() + "&p=" + encrypt);
            return R.ok("发送邮件成功");
        } catch (Exception e) {
            return R.error("发送邮件失败");
        }
    }

    @Override
    public R logout(HttpServletRequest request) {
        Object user = RequestContextHolder.getRequestAttributes().getAttribute(WebFilter.KEY, RequestAttributes.SCOPE_REQUEST);
        if (Objects.nonNull(user)) {
            RequestContextHolder.getRequestAttributes().setAttribute(WebFilter.KEY, null, RequestAttributes.SCOPE_REQUEST);
            final Cookie[] cookies = request.getCookies();
            try {
                final Cookie login_token1 = WebUtils.getCookie(request, "login_token");
                final ConcurrentMap<String, Object> stringObjectConcurrentMap = GuavaCacheUtils.cache.asMap();
                System.out.println(stringObjectConcurrentMap);
                GuavaCacheUtils.cache.invalidate("LOGIN_TOKEN_" + login_token1.getValue());
                request.logout();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
        return R.ok("登出成功");
    }


    private R checkEmail(String userName) {
        if (StringUtils.isEmpty(REGEXP)) {
            return R.error("系统异常");
        }
        boolean flag = StringUtils.checkMail(REGEXP, userName);
        return flag ? R.ok() : R.error("邮箱格式不正确,请输入正确的邮箱格式");
    }

    @Cacheable(cacheNames = "login_token", key = "#loginToken")
    public SysUserEntity resolveUserByToken(String loginToken) throws Exception {
        log.info("没有命中缓存...loginToken={}", loginToken);
        //看缓存是否有
        try {
            Object result = GuavaCacheUtils.cache.get(loginToken);
            if (result == null) {
                return null;
            } else {
                SysUserEntity sysUserEntity = JSON.parseObject(JSON.toJSONString(result), SysUserEntity.class);
                return sysUserEntity;
            }
        } catch (Exception e) {
            log.error("异常{}", e);
            throw e;
        }
    }


    /**
     * 操作日志
     *
     * @param operationLogDto
     */
    @Override
    public void addUserOperationLog(BizLogDto operationLogDto) {
        bizLogDao.addUserOperationLog(operationLogDto);
    }
}