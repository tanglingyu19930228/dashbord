package com.search.biz;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.search.common.utils.DateUtils;
import com.search.common.utils.R;
import com.search.dao.SysRoleDao;
import com.search.entity.RoleEntity;
import com.search.entity.SysRoleEntity;
import com.search.entity.SysUserEntity;
import com.search.entity.UserRoleResp;
import com.search.vo.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class RoleService {

    @Autowired
    SysRoleDao sysRoleDao;

    public R addUserRole(RoleVO roleVO) {
        try {
            if(Objects.isNull(roleVO)||Objects.isNull(roleVO.getSysRoleEntityList())){
                log.error("用户权限添加时，未填写参数");
                return R.error("请填写请求参数");
            }
            List<SysRoleEntity> rolePage = roleVO.getSysRoleEntityList();
            Integer userId = roleVO.getUserId();
            QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("user_id",userId).ge("del_flag",0);
            List<SysRoleEntity> roleDb = this.sysRoleDao.selectList(queryWrapper);
            Map<Integer, List<SysRoleEntity>> dbMap = roleDb.stream().collect(Collectors.groupingBy(SysRoleEntity::getId));
            Map<Integer, List<SysRoleEntity>> pageMap = rolePage.stream().collect(Collectors.groupingBy(SysRoleEntity::getId));

            List<SysRoleEntity> create = new ArrayList<>();
            List<SysRoleEntity> delete = new ArrayList<>();
            for (Map.Entry<Integer, List<SysRoleEntity>> dbEntry : dbMap.entrySet()) {
                List<SysRoleEntity> roleEntityList = pageMap.get(dbEntry.getKey());
                if(roleEntityList == null){
                    delete.addAll(dbEntry.getValue());
                }
            }
            for (Map.Entry<Integer, List<SysRoleEntity>> pageEntry : pageMap.entrySet()) {
                List<SysRoleEntity> roleEntityList = dbMap.get(pageEntry.getKey());
                if(roleEntityList == null){
                    create.addAll(pageEntry.getValue());
                }
            }
            if(!CollectionUtils.isEmpty(create)){
                create.forEach(item->{
                    item.setDelFlag(0);
                    item.setCreateBy(-1);
                    item.setCreateDate(DateUtils.getNowDate());
                    final int insert = this.sysRoleDao.insert(item);
                    log.info("创建用户权限：" + JSON.toJSONString(item));
                });
            }
            if(!CollectionUtils.isEmpty(delete)){
                create.forEach(item->{
                    item.setDelFlag(1);
                    final int insert = this.sysRoleDao.updateById(item);
                    log.info("删除用户权限：" + JSON.toJSONString(item));
                });
            }
            return R.ok("添加权限成功");
        }catch (Exception e){
            return R.error("填加权限失败");
        }
    }

    public R selectRoleList(RoleVO roleVO) {
        if(Objects.isNull(roleVO)||Objects.isNull(roleVO.getSysRoleEntityList())){
            log.error("用户权限添加时，未填写参数");
            return R.error("请填写请求参数");
        }
        try {
            QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("del_flag",0);
            List<SysRoleEntity> allRole = this.sysRoleDao.selectList(queryWrapper);
            queryWrapper.ge("del_flag",0).ge("user_id",roleVO.getUserId());
            List<SysRoleEntity> userRole = this.sysRoleDao.selectList(queryWrapper);
            for (SysRoleEntity allOne : allRole){
                for (SysRoleEntity userOne : userRole){
                    if(allOne.getId().equals(userOne.getId())){
                        allOne.setShow(true);
                    }
                }
            }
            final List<SysRoleEntity> roleEntityList = this.convertTree(allRole, -1);
            return R.ok(roleEntityList);
        } catch (Exception e) {
            return R.error("服务器异常");
        }
    }

    /**
     * 此处 -1 表示根 即 品牌 产品 其他
     * @param roleList 权限集合
     * @param parentId 父id
     * @return 递归返回树
     */
    private List<SysRoleEntity> convertTree(List<SysRoleEntity> roleList,Integer parentId){
        List<SysRoleEntity> result =new ArrayList<>();
        List<SysRoleEntity> temp  = new ArrayList<>();
        for (SysRoleEntity one : roleList) {
            if (one.getParentId().equals(parentId)) {
                temp = convertTree(roleList, one.getId());
                if (!temp.isEmpty()) {
                    one.setList(temp);
                }
                result.add(one);
            }

        }
        return  result;
    }
}
