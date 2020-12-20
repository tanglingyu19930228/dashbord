package com.search.dao;

import com.search.entity.SysMediaTypeEntity;

import java.util.List;

/**
 * SysMediaTypeDao接口
 *
 * @date 2020-12-20 11:36:10
 */
public interface SysMediaTypeDao {
    /**
     * 查询SysMediaTypeEntity
     *
     * @param sysMediaTypeEntity
     * @return SysMediaTypeEntity
     */
    public SysMediaTypeEntity selectSysMediaTypeOne(SysMediaTypeEntity sysMediaTypeEntity);

    /**
     * 查询SysMediaTypeEntity列表
     *
     * @param sysMediaTypeEntity
     * @return SysMediaTypeEntity集合
     */
    public List<SysMediaTypeEntity> selectSysMediaTypeList(SysMediaTypeEntity sysMediaTypeEntity);

    /**
     * 新增SysMediaTypeEntity
     *
     * @param sysMediaTypeEntity
     * @return 结果
     */
    public int insertSysMediaType(List<SysMediaTypeEntity> list);

    /**
     * 修改sysMediaType
     *
     * @param sysMediaTypeEntity
     * @return 结果
     */
    public int updateSysMediaType(List<SysMediaTypeEntity> list);
}
