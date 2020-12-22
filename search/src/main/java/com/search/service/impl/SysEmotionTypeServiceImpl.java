package com.search.service.impl;

import java.util.List;
import java.util.ArrayList;

import com.search.dao.SysEmotionTypeDao;
import com.search.entity.SysEmotionTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.search.service.ISysEmotionTypeService;
import lombok.extern.slf4j.Slf4j;

/**
 * SysEmotionTypeService业务层处理
 *
 * @author Administrator
 * @date 2020-12-22 00:51:05
 */
@Service
@Slf4j
public class SysEmotionTypeServiceImpl implements ISysEmotionTypeService {
    @Autowired
    private SysEmotionTypeDao sysEmotionTypeDao;

    /**
     * 查询SysEmotionTypeEntity
     *
     * @param  id id
     * @return SysEmotionTypeEntity
     */
    @Override
    public SysEmotionTypeEntity selectSysEmotionTypeById(Integer id) {
        SysEmotionTypeEntity sysEmotionTypeEntity = new SysEmotionTypeEntity();
        sysEmotionTypeEntity.setId(id);
        return this.selectSysEmotionTypeOne(sysEmotionTypeEntity);
    }

    /**
     * 查询SysEmotionTypeEntity
     *
     * @param sysEmotionTypeEntity sysEmotionTypeEntity
     * @return SysEmotionTypeEntity
     */
    @Override
    public SysEmotionTypeEntity selectSysEmotionTypeOne(SysEmotionTypeEntity sysEmotionTypeEntity) {
    	try{
        	return sysEmotionTypeDao.selectSysEmotionTypeOne(sysEmotionTypeEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return null;
		}
    }

    /**
     * 查询SysEmotionType列表
     *
     * @param sysEmotionTypeEntity sysEmotionTypeEntity
     * @return SysEmotionTypeEntityList 列表
     */
    @Override
    public List<SysEmotionTypeEntity> selectSysEmotionTypeList(SysEmotionTypeEntity sysEmotionTypeEntity) {
    	try{
        	return sysEmotionTypeDao.selectSysEmotionTypeList(sysEmotionTypeEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return new ArrayList<>();
		}
    }

    /**
     * 新增SysEmotionTypeEntity
     *
     * @param sysEmotionTypeEntity sysEmotionTypeEntity
     * @return 结果
     */
    @Override
    public int insertSysEmotionType(SysEmotionTypeEntity sysEmotionTypeEntity) {
        List<SysEmotionTypeEntity> list = new ArrayList<>();
        list.add(sysEmotionTypeEntity);
        return this.insertSysEmotionTypeList(list);
    }

    /**
     * 新增sysEmotionTypeEntity
     *
     * @param list list
     * @return 结果
     */
    @Override
    public int insertSysEmotionTypeList(List<SysEmotionTypeEntity> list) {
		try{
        	return sysEmotionTypeDao.insertSysEmotionType(list);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

    /**
     * 修改SysEmotionTypeEntity
     *
     * @param sysEmotionTypeEntity sysEmotionTypeEntity
     * @return 结果
     */
    @Override
    public int updateSysEmotionType(SysEmotionTypeEntity sysEmotionTypeEntity) {
        List<SysEmotionTypeEntity> list = new ArrayList<>();
        list.add(sysEmotionTypeEntity);
        return this.updateSysEmotionType(list);
    }

    /**
     * 修改SysEmotionTypeEntity
     *
     * @param sysEmotionTypeEntityList sysEmotionTypeEntityList
     * @return 结果
     */
    @Override
    public int updateSysEmotionType(List<SysEmotionTypeEntity> sysEmotionTypeEntityList) {
		try{
	        return sysEmotionTypeDao.updateSysEmotionType(sysEmotionTypeEntityList);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

}
