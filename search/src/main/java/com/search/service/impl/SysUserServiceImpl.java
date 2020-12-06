package com.search.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.search.bean.SpringConfiguration;
import com.search.common.utils.R;
import com.search.common.utils.StringUtils;
import com.search.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.search.dao.SysUserDao;
import com.search.service.SysUserService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Administrator
 */
@Service("sysUserService")
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {


    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private SpringConfiguration springConfiguration;

    private String REGEXP = null;

    @PostConstruct
    public void initParameters() {
        REGEXP = springConfiguration.getEmail();
    }


    @Override
    public R login(SysUserEntity sysUserEntity) {
        String userName = sysUserEntity.getUserName();
        if (StringUtils.isNotEmpty(userName)) {
            //校验邮箱
            R r = checkEmail(userName);
            if (r != null) {
                return r;
            }
            //根据用户名查用户
            SysUserEntity result = sysUserDao.selectOneByUserName(userName);
            if (result == null) {
                log.error("用户名不存在:userName={}", sysUserEntity.getUserName());
                return R.error("用户名不存在,请输入正确的用户名");
            }
            if (result.getDelFlag() == 1) {
                return R.error("该用户已经被删除");
            }
            //校验password
            String password = result.getPassword();
            String webPassword=sysUserEntity.getPassword();
            if (StringUtils.isEmpty(webPassword)) {
                return R.error("用户密码不能为空");
            }
            if (password.equals(sysUserEntity.getPassword())) {
                return R.ok("登录成功");
            }
            return R.ok("密码错误,请输入正确的密码");
        }
        log.info("用户名不能为空");
        return R.error("用户名不能为空,请输入用户名");
    }

    @Override
    public SysUserEntity getUserInfoByUserId(Integer id) {
        return sysUserDao.getUserInfoByUserId(id);
    }

    /**
     * 用户入库
     *
     * @param sysUser
     * @return
     */
    @Override
    public int saveUser(SysUserEntity sysUser) {
        return 0;
    }


    @Override
    public R queryByUserNameOrId(UserQueryReq queryReq) {
        UserRoleResp resp=sysUserDao.queryByUserNameOrId(queryReq);
        return R.ok(resp);
    }

    @Override
    public R queryByRoleNameOrId(RoleQueryReq queryReq) {
        RoleUserResp resp=sysUserDao.queryByRoleNameOrId(queryReq);
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
        PageHelper.startPage(pageNum,pageSize);
        List<SysUserEntity> sysUserEntities = sysUserDao.listByPage();
        if(CollectionUtils.isNotEmpty(sysUserEntities)){
            return new PageInfo<>(sysUserEntities);
        }
        return new PageInfo<>(Collections.emptyList());
    }

    private R checkEmail(String userName) {
        if (StringUtils.isEmpty(REGEXP)) {
            return R.error("系统异常");
        }
        Pattern p = Pattern.compile(REGEXP);
        Matcher m = p.matcher(userName);
        if (m.matches()) {
            return null;
        } else {
            return R.error("邮箱格式不正确,请输入正确的邮箱格式");
        }
    }
}