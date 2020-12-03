package com.search.service.impl;

import com.search.bean.SpringConfiguration;
import com.search.common.utils.AESUtil;
import com.search.common.utils.R;
import com.search.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.search.dao.SysUserDao;
import com.search.entity.SysUserEntity;
import com.search.service.SysUserService;

import javax.annotation.Resource;


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

    @Override
    public R login(SysUserEntity sysUserEntity) {
        String userName = sysUserEntity.getUserName();
        if (StringUtils.isNotEmpty(userName)) {
            //根据用户名查用户
            SysUserEntity result = sysUserDao.selectOneByUserName(userName);
            if (result == null) {
                log.error("用户名不存在:userName={}", sysUserEntity.getUserName());
                return R.error("用户名不存在,请输入正确的用户名");
            }
            //校验password
            String aesKey = springConfiguration.getKey();
            String password = result.getPassword();
            if (StringUtils.isEmpty(aesKey)) {
                return R.error("系统异常");
            }
            if (StringUtils.isEmpty(password)) {
                return R.error("密码不能为空");
            }
            try {
                String originPassword = AESUtil.decrypt(password, aesKey);
                if (password.equals(originPassword)) {
                    return R.ok("登录成功");
                }
                return R.ok("密码错误,请输入正确的密码");
            } catch (Exception e) {
                log.info("解密异常");
                return R.error("系统异常");
            }
        }
        log.info("用户名不能为空");
        return R.error("用户名不能为空,请输入用户名");
    }
}