package com.search.service;

import com.search.entity.SysArticleDetailEntity;

import java.util.List;

/**
 * sysArticleDetailService接口
 *
 * @author chenshun
 * @date 2020-12-09 23:40:13
 */
public interface ISysArticleDetailService {
    /**
     * 查询SysArticleDetailEntity
     *
     * @param SysArticleDetailID
     * @return SysArticleDetailEntity
     */
    public SysArticleDetailEntity selectSysArticleDetailById(Integer publisher_id);

    /**
     * 查询一个SysArticleDetailEntity
     *
     * @param SysArticleDetailEntity sysArticleDetailEntity
     * @return SysArticleDetailEntity
     */
    public SysArticleDetailEntity selectSysArticleDetailOne(SysArticleDetailEntity sysArticleDetailEntity);

    /**
     * 查询SysArticleDetail列表
     *
     * @param SysArticleDetail sysArticleDetail
     * @return SysArticleDetail集合
     */
    public List<SysArticleDetailEntity> selectSysArticleDetailList(SysArticleDetailEntity sysArticleDetailEntity);

    /**
     * 批量新增SysArticleDetailEntity
     *
     * @param sysArticleDetailEntityList
     * @return 结果
     */
    public int insertSysArticleDetailList(List<SysArticleDetailEntity> list);

    /**
     * 新增SysArticleDetailEntity
     *
     * @param sysArticleDetailEntity
     * @return 结果
     */
    public int insertSysArticleDetail(SysArticleDetailEntity sysArticleDetailEntity);

    /**
     * 批量修改SysArticleDetailEntity
     *
     * @param list
     * @return 结果
     */
    public int updateSysArticleDetail(List<SysArticleDetailEntity> list);

    /**
     * 修改SysArticleDetailEntity
     *
     * @param SysArticleDetailEntity
     * @return 结果
     */
    public int updateSysArticleDetail(SysArticleDetailEntity sysArticleDetailEntity);

}
