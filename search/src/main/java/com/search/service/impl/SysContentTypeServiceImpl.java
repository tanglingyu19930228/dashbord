package com.search.service.impl;

import java.util.List;
import java.util.ArrayList;

import com.search.dao.SysContentTypeDao;
import com.search.entity.SysContentTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.search.service.ISysContentTypeService;
import lombok.extern.slf4j.Slf4j;

/**
 * SysContentTypeService业务层处理
 *
 * @date 2020-12-20 11:36:10
 */
@Service
@Slf4j
public class SysContentTypeServiceImpl implements ISysContentTypeService {
    @Autowired
    private SysContentTypeDao sysContentTypeDao;

    /**
     * 查询SysContentTypeEntity
     *
     * @param
     * @return SysContentTypeEntity
     */
    @Override
    public SysContentTypeEntity selectSysContentTypeById(Integer id) {
        SysContentTypeEntity sysContentTypeEntity = new SysContentTypeEntity();
        sysContentTypeEntity.setId(id);
        return this.selectSysContentTypeOne(sysContentTypeEntity);
    }

    /**
     * 查询SysContentTypeEntity
     *
     * @param sysContentTypeEntity
     * @return SysContentTypeEntity
     */
    @Override
    public SysContentTypeEntity selectSysContentTypeOne(SysContentTypeEntity sysContentTypeEntity) {
    	try{
        	return sysContentTypeDao.selectSysContentTypeOne(sysContentTypeEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return null;
		}
    }

    /**
     * 查询SysContentType列表
     *
     * @param sysContentTypeEntity
     * @return SysContentTypeEntityList 列表
     */
    @Override
    @Cacheable(cacheNames = "selectSysContentTypeList")
    public List<SysContentTypeEntity> selectSysContentTypeList(SysContentTypeEntity sysContentTypeEntity) {
    	try{
        	return sysContentTypeDao.selectSysContentTypeList(sysContentTypeEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return new ArrayList<SysContentTypeEntity>();
		}
    }

    /**
     * 新增SysContentTypeEntity
     *
     * @param sysContentTypeEntity
     * @return 结果
     */
    @Override
    public int insertSysContentType(SysContentTypeEntity sysContentTypeEntity) {
        List<SysContentTypeEntity> list = new ArrayList<SysContentTypeEntity>();
        list.add(sysContentTypeEntity);
        return this.insertSysContentTypeList(list);
    }

    /**
     * 新增sysContentTypeEntity
     *
     * @param
     * @return 结果
     */
    @Override
    public int insertSysContentTypeList(List<SysContentTypeEntity> list) {
		try{
        	return sysContentTypeDao.insertSysContentType(list);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

    /**
     * 修改SysContentTypeEntity
     *
     * @param sysContentTypeEntity
     * @return 结果
     */
    @Override
    public int updateSysContentType(SysContentTypeEntity sysContentTypeEntity) {
        List<SysContentTypeEntity> list = new ArrayList<SysContentTypeEntity>();
        list.add(sysContentTypeEntity);
        return this.updateSysContentType(list);
    }

    /**
     * 修改SysContentTypeEntity
     *
     * @param sysContentTypeEntityList
     * @return 结果
     */
    @Override
    public int updateSysContentType(List<SysContentTypeEntity> sysContentTypeEntityList) {
		try{
	        return sysContentTypeDao.updateSysContentType(sysContentTypeEntityList);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

}
