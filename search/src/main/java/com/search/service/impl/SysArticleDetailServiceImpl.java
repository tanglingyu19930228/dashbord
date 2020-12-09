package com.search.service.impl;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.search.dao.SysArticleDetailDao;
import com.search.entity.SysArticleDetailEntity;
import com.search.service.ISysArticleDetailService;
import com.search.common.text.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * SysArticleDetailService业务层处理
 *
 * @author Administrator
 * @date 2020-12-09 23:40:13
 */
@Service
@Slf4j
public class SysArticleDetailServiceImpl implements ISysArticleDetailService {
    @Autowired
    private SysArticleDetailDao sysArticleDetailDao;

    /**
     * 查询SysArticleDetailEntity
     *
     * @param  id id
     * @return SysArticleDetailEntity
     */
    @Override
    public SysArticleDetailEntity selectSysArticleDetailById(Integer id) {
        SysArticleDetailEntity sysArticleDetailEntity = new SysArticleDetailEntity();
        sysArticleDetailEntity.setPublisherId(id);
        return this.selectSysArticleDetailOne(sysArticleDetailEntity);
    }

    /**
     * 查询SysArticleDetailEntity
     *
     * @param sysArticleDetailEntity 请求实体
     * @return SysArticleDetailEntity
     */
    @Override
    public SysArticleDetailEntity selectSysArticleDetailOne(SysArticleDetailEntity sysArticleDetailEntity) {
    	try{
        	return sysArticleDetailDao.selectSysArticleDetailOne(sysArticleDetailEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return null;
		}
    }

    /**
     * 查询SysArticleDetail列表
     *
     * @param sysArticleDetailEntity  请求实体
     * @return SysArticleDetailEntityList 列表
     */
    @Override
    public List<SysArticleDetailEntity> selectSysArticleDetailList(SysArticleDetailEntity sysArticleDetailEntity) {
    	try{
        	return sysArticleDetailDao.selectSysArticleDetailList(sysArticleDetailEntity);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return new ArrayList<SysArticleDetailEntity>();
		}
    }

    /**
     * 新增SysArticleDetailEntity
     *
     * @param sysArticleDetailEntity 请求实体
     * @return 结果
     */
    @Override
    public int insertSysArticleDetail(SysArticleDetailEntity sysArticleDetailEntity) {
        List<SysArticleDetailEntity> list = new ArrayList<>();
        list.add(sysArticleDetailEntity);
        return this.insertSysArticleDetailList(list);
    }

    /**
     * 新增sysArticleDetailEntity
     *
     * @param list s
     * @return 结果
     */
    @Override
    public int insertSysArticleDetailList(List<SysArticleDetailEntity> list) {
		try{
        	return sysArticleDetailDao.insertSysArticleDetail(list);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

    /**
     * 修改SysArticleDetailEntity
     *
     * @param sysArticleDetailEntity 实体
     * @return 结果
     */
    @Override
    public int updateSysArticleDetail(SysArticleDetailEntity sysArticleDetailEntity) {
        List<SysArticleDetailEntity> list = new ArrayList<SysArticleDetailEntity>();
        list.add(sysArticleDetailEntity);
        return this.updateSysArticleDetail(list);
    }

    /**
     * 修改SysArticleDetailEntity
     *
     * @param sysArticleDetailEntityList list
     * @return 结果
     */
    @Override
    public int updateSysArticleDetail(List<SysArticleDetailEntity> sysArticleDetailEntityList) {
		try{
	        return sysArticleDetailDao.updateSysArticleDetail(sysArticleDetailEntityList);
		} catch (Exception e) {
			log.error("数据库异常:",e);
			return 0;
		}
    }

}
