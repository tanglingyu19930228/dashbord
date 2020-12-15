package com.search.dao;

import com.search.entity.SysArticleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SysArticleDao接口
 *
 * @date 2020-12-09 23:40:13
 */
public interface SysArticleDao {
    /**
     * 查询SysArticleEntity
     *
     * @param sysArticleEntity sysArticleEntity
     * @return SysArticleEntity
     */
    SysArticleEntity selectSysArticleOne(SysArticleEntity sysArticleEntity);

    /**
     * 查询SysArticleEntity列表
     *
     * @param sysArticleEntity sysArticleEntity
     * @return SysArticleEntity集合
     */
    List<SysArticleEntity> selectSysArticleList(SysArticleEntity sysArticleEntity);

    /**
     * 新增SysArticleEntity
     *
     * @param list list
     * @return 结果
     */
    int insertSysArticle(List<SysArticleEntity> list);

    /**
     * 修改sysArticle
     *
     * @param list list
     * @return 结果
     */
    int updateSysArticle(List<SysArticleEntity> list);
}
