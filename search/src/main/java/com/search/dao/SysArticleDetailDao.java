package com.search.dao;

import com.search.entity.SysArticleDetailEntity;
import java.util.List;

/**
 * SysArticleDetailDao接口
 *
 * @date 2020-12-09 23:40:13
 */
public interface SysArticleDetailDao {
    /**
     * 查询SysArticleDetailEntity
     *
     * @param sysArticleDetailEntity
     * @return SysArticleDetailEntity
     */
    public SysArticleDetailEntity selectSysArticleDetailOne(SysArticleDetailEntity sysArticleDetailEntity);

    /**
     * 查询SysArticleDetailEntity列表
     *
     * @param sysArticleDetailEntity
     * @return SysArticleDetailEntity集合
     */
    public List<SysArticleDetailEntity> selectSysArticleDetailList(SysArticleDetailEntity sysArticleDetailEntity);

    /**
     * 新增SysArticleDetailEntity
     *
     * @param sysArticleDetailEntity
     * @return 结果
     */
    public int insertSysArticleDetail(List<SysArticleDetailEntity> list);

    /**
     * 修改sysArticleDetail
     *
     * @param sysArticleDetailEntity
     * @return 结果
     */
    public int updateSysArticleDetail(List<SysArticleDetailEntity> list);
}
