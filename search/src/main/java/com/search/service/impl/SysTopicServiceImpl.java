package com.search.service.impl;

import java.util.List;
import java.util.ArrayList;

import com.search.dao.SysTopicDao;
import com.search.entity.SysTopicEntity;
import com.search.service.ISysTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.search.common.text.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * SysTopicService业务层处理
 *
 * @date 2020-12-20 11:36:10
 */
@Service
@Slf4j
public class SysTopicServiceImpl implements ISysTopicService {
    @Autowired
    private SysTopicDao sysTopicDao;

    /**
     * 查询SysTopicEntity
     *
     * @param  ID
     * @return SysTopicEntity
     */
    @Override
    public SysTopicEntity selectSysTopicById(Integer id) {
        SysTopicEntity sysTopicEntity = new SysTopicEntity();
        sysTopicEntity.setId(id);
        return this.selectSysTopicOne(sysTopicEntity);
    }

    /**
     * 查询SysTopicEntity
     *
     * @param sysTopicEntity
     * @return SysTopicEntity
     */
    @Override
    public SysTopicEntity selectSysTopicOne(SysTopicEntity sysTopicEntity) {
    	try{
        	return sysTopicDao.selectSysTopicOne(sysTopicEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return null;
		}
    }

    /**
     * 查询SysTopic列表
     *
     * @param sysTopicEntity
     * @return SysTopicEntityList 列表
     */
    @Override
    @Cacheable(cacheNames = "selectSysTopicList")
    public List<SysTopicEntity> selectSysTopicList(SysTopicEntity sysTopicEntity) {
    	try{
        	return sysTopicDao.selectSysTopicList(sysTopicEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return new ArrayList<SysTopicEntity>();
		}
    }

    /**
     * 新增SysTopicEntity
     *
     * @param sysTopicEntity
     * @return 结果
     */
    @Override
    public int insertSysTopic(SysTopicEntity sysTopicEntity) {
        List<SysTopicEntity> list = new ArrayList<SysTopicEntity>();
        list.add(sysTopicEntity);
        return this.insertSysTopicList(list);
    }

    /**
     * 新增sysTopicEntity
     *
     * @param
     * @return 结果
     */
    @Override
    public int insertSysTopicList(List<SysTopicEntity> list) {
		try{
        	return sysTopicDao.insertSysTopic(list);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

    /**
     * 修改SysTopicEntity
     *
     * @param sysTopicEntity
     * @return 结果
     */
    @Override
    public int updateSysTopic(SysTopicEntity sysTopicEntity) {
        List<SysTopicEntity> list = new ArrayList<SysTopicEntity>();
        list.add(sysTopicEntity);
        return this.updateSysTopic(list);
    }

    /**
     * 修改SysTopicEntity
     *
     * @param sysTopicEntityList
     * @return 结果
     */
    @Override
    public int updateSysTopic(List<SysTopicEntity> sysTopicEntityList) {
		try{
	        return sysTopicDao.updateSysTopic(sysTopicEntityList);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

}
