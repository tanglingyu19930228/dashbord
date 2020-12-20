package com.search.service.impl;

import java.util.List;
import java.util.ArrayList;

import com.search.dao.SysMediaTypeDao;
import com.search.entity.SysMediaTypeEntity;
import com.search.service.ISysMediaTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * SysMediaTypeService业务层处理
 *
 * @date 2020-12-20 11:36:10
 */
@Service
@Slf4j
public class SysMediaTypeServiceImpl implements ISysMediaTypeService {
    @Autowired
    private SysMediaTypeDao sysMediaTypeDao;

    /**
     * 查询SysMediaTypeEntity
     *
     * @param  ID
     * @return SysMediaTypeEntity
     */
    @Override
    public SysMediaTypeEntity selectSysMediaTypeById(Integer id) {
        SysMediaTypeEntity sysMediaTypeEntity = new SysMediaTypeEntity();
        sysMediaTypeEntity.setId(id);
        return this.selectSysMediaTypeOne(sysMediaTypeEntity);
    }

    /**
     * 查询SysMediaTypeEntity
     *
     * @param sysMediaTypeEntity
     * @return SysMediaTypeEntity
     */
    @Override
    public SysMediaTypeEntity selectSysMediaTypeOne(SysMediaTypeEntity sysMediaTypeEntity) {
    	try{
        	return sysMediaTypeDao.selectSysMediaTypeOne(sysMediaTypeEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return null;
		}
    }

    /**
     * 查询SysMediaType列表
     *
     * @param sysMediaTypeEntity
     * @return SysMediaTypeEntityList 列表
     */
    @Override
    public List<SysMediaTypeEntity> selectSysMediaTypeList(SysMediaTypeEntity sysMediaTypeEntity) {
    	try{
        	return sysMediaTypeDao.selectSysMediaTypeList(sysMediaTypeEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return new ArrayList<SysMediaTypeEntity>();
		}
    }

    /**
     * 新增SysMediaTypeEntity
     *
     * @param sysMediaTypeEntity
     * @return 结果
     */
    @Override
    public int insertSysMediaType(SysMediaTypeEntity sysMediaTypeEntity) {
        List<SysMediaTypeEntity> list = new ArrayList<SysMediaTypeEntity>();
        list.add(sysMediaTypeEntity);
        return this.insertSysMediaTypeList(list);
    }

    /**
     * 新增sysMediaTypeEntity
     *
     * @param
     * @return 结果
     */
    @Override
    public int insertSysMediaTypeList(List<SysMediaTypeEntity> list) {
		try{
        	return sysMediaTypeDao.insertSysMediaType(list);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

    /**
     * 修改SysMediaTypeEntity
     *
     * @param sysMediaTypeEntity
     * @return 结果
     */
    @Override
    public int updateSysMediaType(SysMediaTypeEntity sysMediaTypeEntity) {
        List<SysMediaTypeEntity> list = new ArrayList<SysMediaTypeEntity>();
        list.add(sysMediaTypeEntity);
        return this.updateSysMediaType(list);
    }

    /**
     * 修改SysMediaTypeEntity
     *
     * @param sysMediaTypeEntityList
     * @return 结果
     */
    @Override
    public int updateSysMediaType(List<SysMediaTypeEntity> sysMediaTypeEntityList) {
		try{
	        return sysMediaTypeDao.updateSysMediaType(sysMediaTypeEntityList);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

}
