package com.search.service.impl;

import java.util.List;
import java.util.ArrayList;

import com.search.dao.SysKeyDao;
import com.search.entity.SysKeyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.search.service.ISysKeyService;
import com.search.common.text.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * SysKeyService业务层处理
 *
 * @date 2020-12-22 00:02:33
 */
@Service
@Slf4j
public class SysKeyServiceImpl implements ISysKeyService {
    @Autowired
    private SysKeyDao sysKeyDao;

    /**
     * 查询SysKeyEntity
     *
     * @param  id id
     * @return SysKeyEntity
     */
    @Override
    public SysKeyEntity selectSysKeyById(Integer id) {
        SysKeyEntity sysKeyEntity = new SysKeyEntity();
        sysKeyEntity.setId(id);
        return this.selectSysKeyOne(sysKeyEntity);
    }

    /**
     * 查询SysKeyEntity
     *
     * @param sysKeyEntity sysKeyEntity
     * @return SysKeyEntity
     */
    @Override
    public SysKeyEntity selectSysKeyOne(SysKeyEntity sysKeyEntity) {
    	try{
        	return sysKeyDao.selectSysKeyOne(sysKeyEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return null;
		}
    }

    /**
     * 查询SysKey列表
     *
     * @param sysKeyEntity sysKeyEntity
     * @return SysKeyEntityList 列表
     */
    @Override
    public List<SysKeyEntity> selectSysKeyList(SysKeyEntity sysKeyEntity) {
    	try{
        	return sysKeyDao.selectSysKeyList(sysKeyEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return new ArrayList<SysKeyEntity>();
		}
    }

    /**
     * 新增SysKeyEntity
     *
     * @param sysKeyEntity sysKeyEntity
     * @return 结果
     */
    @Override
    public int insertSysKey(SysKeyEntity sysKeyEntity) {
        List<SysKeyEntity> list = new ArrayList<SysKeyEntity>();
        list.add(sysKeyEntity);
        return this.insertSysKeyList(list);
    }

    /**
     * 新增sysKeyEntity
     *
     * @param list list
     * @return 结果
     */
    @Override
    public int insertSysKeyList(List<SysKeyEntity> list) {
		try{
        	return sysKeyDao.insertSysKey(list);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

    /**
     * 修改SysKeyEntity
     *
     * @param sysKeyEntity sysKeyEntity
     * @return 结果
     */
    @Override
    public int updateSysKey(SysKeyEntity sysKeyEntity) {
        List<SysKeyEntity> list = new ArrayList<SysKeyEntity>();
        list.add(sysKeyEntity);
        return this.updateSysKey(list);
    }

    /**
     * 修改SysKeyEntity
     *
     * @param sysKeyEntityList sysKeyEntityList
     * @return 结果
     */
    @Override
    public int updateSysKey(List<SysKeyEntity> sysKeyEntityList) {
		try{
	        return sysKeyDao.updateSysKey(sysKeyEntityList);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

}
