package com.search.service;

import com.search.entity.SysArticleEntity;

import java.util.List;

/**
 * sysArticleService接口
 *
 * @author chenshun
 * @date 2020-12-09 23:40:13
 */
public interface ISysArticleService {
    /**
     * 查询SysArticleEntity
     *
     * @param id id
     * @return SysArticleEntity
     */
    public SysArticleEntity selectSysArticleById(Integer id);

    /**
     * 查询一个SysArticleEntity
     *
     * @param sysArticleEntity sysArticleEntity
     * @return SysArticleEntity
     */
    public SysArticleEntity selectSysArticleOne(SysArticleEntity sysArticleEntity);

    /**
     * 查询SysArticle列表
     *
     * @param sysArticleEntity sysArticleEntity
     * @return SysArticle集合
     */
    public List<SysArticleEntity> selectSysArticleList(SysArticleEntity sysArticleEntity);

    /**
     * 批量新增SysArticleEntity
     *
     * @param list list
     * @return 结果
     */
    public int insertSysArticleList(List<SysArticleEntity> list);

    /**
     * 新增SysArticleEntity
     *
     * @param sysArticleEntity sysArticleEntity
     * @return 结果
     */
    public int insertSysArticle(SysArticleEntity sysArticleEntity);

    /**
     * 批量修改SysArticleEntity
     *
     * @param list 集合
     * @return 结果
     */
    public int updateSysArticle(List<SysArticleEntity> list);

    /**
     * 修改SysArticleEntity
     *
     * @param sysArticleEntity sysArticleEntity
     * @return 结果
     */
    public int updateSysArticle(SysArticleEntity sysArticleEntity);

}
