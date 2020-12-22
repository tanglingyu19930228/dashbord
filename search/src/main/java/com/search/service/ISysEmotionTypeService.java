package com.search.service;

import com.search.entity.SysEmotionTypeEntity;

import java.util.List;

/**
 * sysEmotionTypeService接口
 *
 * @author chenshun
 * @date 2020-12-22 00:51:05
 */
public interface ISysEmotionTypeService {
    /**
     * 查询SysEmotionTypeEntity
     *
     * @param id id
     * @return SysEmotionTypeEntity
     */
    public SysEmotionTypeEntity selectSysEmotionTypeById(Integer id);

    /**
     * 查询一个SysEmotionTypeEntity
     *
     * @param sysEmotionTypeEntity sysEmotionTypeEntity
     * @return SysEmotionTypeEntity
     */
    public SysEmotionTypeEntity selectSysEmotionTypeOne(SysEmotionTypeEntity sysEmotionTypeEntity);

    /**
     * 查询SysEmotionType列表
     *
     * @param sysEmotionTypeEntity sysEmotionTypeEntity
     * @return SysEmotionType集合
     */
    public List<SysEmotionTypeEntity> selectSysEmotionTypeList(SysEmotionTypeEntity sysEmotionTypeEntity);

    /**
     * 批量新增SysEmotionTypeEntity
     *
     * @param list list
     * @return 结果
     */
    public int insertSysEmotionTypeList(List<SysEmotionTypeEntity> list);

    /**
     * 新增SysEmotionTypeEntity
     *
     * @param sysEmotionTypeEntity sysEmotionTypeEntity
     * @return 结果
     */
    public int insertSysEmotionType(SysEmotionTypeEntity sysEmotionTypeEntity);

    /**
     * 批量修改SysEmotionTypeEntity
     *
     * @param list list
     * @return 结果
     */
    public int updateSysEmotionType(List<SysEmotionTypeEntity> list);

    /**
     * 修改SysEmotionTypeEntity
     *
     * @param sysEmotionTypeEntity sysEmotionTypeEntity
     * @return 结果
     */
    public int updateSysEmotionType(SysEmotionTypeEntity sysEmotionTypeEntity);

}
