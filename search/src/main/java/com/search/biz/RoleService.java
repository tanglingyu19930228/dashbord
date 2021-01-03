package com.search.biz;


import com.alibaba.fastjson.JSON;
import com.search.common.utils.DateUtils;
import com.search.common.utils.R;
import com.search.dao.SysRoleDao;
import com.search.dao.SysRoleUserDao;
import com.search.dao.SysUserDao;
import com.search.entity.RoleEntity;
import com.search.entity.SysRoleEntity;
import com.search.entity.SysRoleUserEntity;
import com.search.entity.SysUserEntity;
import com.search.vo.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Administrator
 */
@Service
@Slf4j
public class RoleService {

    @Autowired
    SysRoleDao sysRoleDao;

    @Autowired
    SysUserDao sysUserDao;

    @Autowired
    SysRoleUserDao sysRoleUserDao;

    public R addUserRole(RoleVO roleVO) {
        try {
            SysUserEntity sysUserEntity1 = this.sysUserDao.selectOneByUserName(roleVO.getUserName());
            if(Objects.isNull(sysUserEntity1)){
                SysUserEntity sysUserEntity = new SysUserEntity();
                sysUserEntity.setDelFlag(0);
                sysUserEntity.setIsAdmin(1);
                sysUserEntity.setUserName(roleVO.getUserName());
                sysUserEntity.setPassword(roleVO.getPassword());
                sysUserEntity.setName(roleVO.getUserName());
                final int i = sysUserDao.saveUser(sysUserEntity);
                sysUserEntity1 = sysUserEntity;
                if(i<0){
                    return R.error("新增用户失败");
                }
            }
            List<SysRoleEntity> rolePage = roleVO.getSysRoleEntityList();
            if(Objects.isNull(roleVO.getSysRoleEntityList())){
                log.error("用户权限添加时，未填写参数");
                return R.error("请填写请求参数");
            }
            Integer userId = sysUserEntity1.getId();
            SysRoleUserEntity sysRoleUserEntity = new SysRoleUserEntity();
            sysRoleUserEntity.setUserId(userId);
            sysRoleUserEntity.setDelFlag(0);
            List<SysRoleUserEntity> listDb = this.sysRoleUserDao.selectList(sysRoleUserEntity);
            List<SysRoleUserEntity> collectPage = rolePage.stream().map(item -> {
                SysRoleUserEntity te = new SysRoleUserEntity();
                te.setDelFlag(0);
                te.setRoleId(item.getId());
                te.setUserId(userId);
                return te;
            }).collect(Collectors.toList());
            Map<Integer, List<SysRoleUserEntity>> dbMap = listDb.stream().collect(Collectors.groupingBy(SysRoleUserEntity::getRoleId));
            Map<Integer, List<SysRoleUserEntity>> pageMap = collectPage.stream().collect(Collectors.groupingBy(SysRoleUserEntity::getRoleId));
//
            List<SysRoleUserEntity> create = new ArrayList<>();
            List<SysRoleUserEntity> delete = new ArrayList<>();
            for (Map.Entry<Integer, List<SysRoleUserEntity>> dbEntry : dbMap.entrySet()) {
                List<SysRoleUserEntity> roleEntityList = pageMap.get(dbEntry.getKey());
                if(roleEntityList == null){
                    delete.addAll(dbEntry.getValue());
                }
            }
            for (Map.Entry<Integer, List<SysRoleUserEntity>> pageEntry : pageMap.entrySet()) {
                List<SysRoleUserEntity> roleEntityList = dbMap.get(pageEntry.getKey());
                if(roleEntityList == null){
                    create.addAll(pageEntry.getValue());
                }
            }
            if(!CollectionUtils.isEmpty(create)){
                create.forEach(item->{
                    final int insert = this.sysRoleUserDao.save(item);
                    log.info("创建用户权限：" + JSON.toJSONString(item));
                });
            }
            if(!CollectionUtils.isEmpty(delete)){
                delete.forEach(item->{
                    sysRoleUserEntity.setDelFlag(1);
                    final int deleteFlag = this.sysRoleUserDao.update(sysRoleUserEntity);
                    log.info("删除用户权限：" + JSON.toJSONString(item));
                });
            }
            return R.ok("添加权限成功");
        }catch (Exception e){
            return R.error("填加权限失败");
        }
    }

    public R selectRoleList(RoleVO roleVO) {
        if(Objects.isNull(roleVO)){
            log.error("用户权限添加时，未填写参数");
            return R.error("请填写请求参数");
        }
        try {
            List<SysRoleEntity> allRole = this.sysRoleDao.selectSysRoleList(new SysRoleEntity());
            SysRoleEntity sysRoleEntity = new SysRoleEntity();
            sysRoleEntity.setCreateBy(roleVO.getUserId());
            List<SysRoleEntity> userRole = this.sysRoleDao.selectRoleListByUser(sysRoleEntity);
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
            log.error("esdf",e);
        }
        return R.error("服务器异常");
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
