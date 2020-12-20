package com.search.service;

import com.search.entity.SysContentTypeEntity;

import java.util.List;

/**
 * sysContentTypeService接口
 *
 * @author chenshun
 * @date 2020-12-20 11:36:10
 */
public interface ISysContentTypeService {
    /**
     * 查询SysContentTypeEntity
     *
     * @param
     * @return SysContentTypeEntity
     */
    public SysContentTypeEntity selectSysContentTypeById(Integer id);

    /**
     * 查询一个SysContentTypeEntity
     *
     * @param SysContentTypeEntity sysContentTypeEntity
     * @return SysContentTypeEntity
     */
    public SysContentTypeEntity selectSysContentTypeOne(SysContentTypeEntity sysContentTypeEntity);

    /**
     * 查询SysContentType列表
     *
     * @param  sysContentType
     * @return SysContentType集合
     */
    public List<SysContentTypeEntity> selectSysContentTypeList(SysContentTypeEntity sysContentTypeEntity);

    /**
     * 批量新增SysContentTypeEntity
     *
     * @param sysContentTypeEntityList
     * @return 结果
     */
    public int insertSysContentTypeList(List<SysContentTypeEntity> list);

    /**
     * 新增SysContentTypeEntity
     *
     * @param sysContentTypeEntity
     * @return 结果
     */
    public int insertSysContentType(SysContentTypeEntity sysContentTypeEntity);

    /**
     * 批量修改SysContentTypeEntity
     *
     * @param list
     * @return 结果
     */
    public int updateSysContentType(List<SysContentTypeEntity> list);

    /**
     * 修改SysContentTypeEntity
     *
     * @param SysContentTypeEntity
     * @return 结果
     */
    public int updateSysContentType(SysContentTypeEntity sysContentTypeEntity);

}
