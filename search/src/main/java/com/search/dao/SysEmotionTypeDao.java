package com.search.dao;

import com.search.entity.SysEmotionTypeEntity;

import java.util.List;

/**
 * SysEmotionTypeDao接口
 *
 * @date 2020-12-22 00:51:05
 */
public interface SysEmotionTypeDao {
    /**
     * 查询SysEmotionTypeEntity
     *
     * @param sysEmotionTypeEntity sysEmotionTypeEntity
     * @return SysEmotionTypeEntity
     */
    public SysEmotionTypeEntity selectSysEmotionTypeOne(SysEmotionTypeEntity sysEmotionTypeEntity);

    /**
     * 查询SysEmotionTypeEntity列表
     *
     * @param sysEmotionTypeEntity sysEmotionTypeEntity
     * @return SysEmotionTypeEntity集合
     */
    public List<SysEmotionTypeEntity> selectSysEmotionTypeList(SysEmotionTypeEntity sysEmotionTypeEntity);

    /**
     * 新增SysEmotionTypeEntity
     *
     * @param list list
     * @return 结果
     */
    public int insertSysEmotionType(List<SysEmotionTypeEntity> list);

    /**
     * 修改sysEmotionType
     *
     * @param list list
     * @return 结果
     */
    public int updateSysEmotionType(List<SysEmotionTypeEntity> list);
}
