package com.search.dao;

import com.search.entity.SysContentTypeEntity;

import java.util.List;

/**
 * SysContentTypeDao接口
 *
 * @date 2020-12-20 11:36:10
 */
public interface SysContentTypeDao {
    /**
     * 查询SysContentTypeEntity
     *
     * @param sysContentTypeEntity
     * @return SysContentTypeEntity
     */
    public SysContentTypeEntity selectSysContentTypeOne(SysContentTypeEntity sysContentTypeEntity);

    /**
     * 查询SysContentTypeEntity列表
     *
     * @param sysContentTypeEntity
     * @return SysContentTypeEntity集合
     */
    public List<SysContentTypeEntity> selectSysContentTypeList(SysContentTypeEntity sysContentTypeEntity);

    /**
     * 新增SysContentTypeEntity
     *
     * @param sysContentTypeEntity
     * @return 结果
     */
    public int insertSysContentType(List<SysContentTypeEntity> list);

    /**
     * 修改sysContentType
     *
     * @param sysContentTypeEntity
     * @return 结果
     */
    public int updateSysContentType(List<SysContentTypeEntity> list);
}
