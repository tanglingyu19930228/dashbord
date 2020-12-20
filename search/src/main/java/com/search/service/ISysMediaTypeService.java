package com.search.service;

import com.search.entity.SysMediaTypeEntity;

import java.util.List;

/**
 * sysMediaTypeService接口
 *
 * @author chenshun
 * @date 2020-12-20 11:36:10
 */
public interface ISysMediaTypeService {
    /**
     * 查询SysMediaTypeEntity
     *
     * @param SysMediaTypeID
     * @return SysMediaTypeEntity
     */
    public SysMediaTypeEntity selectSysMediaTypeById(Integer id);

    /**
     * 查询一个SysMediaTypeEntity
     *
     * @param SysMediaTypeEntity sysMediaTypeEntity
     * @return SysMediaTypeEntity
     */
    public SysMediaTypeEntity selectSysMediaTypeOne(SysMediaTypeEntity sysMediaTypeEntity);

    /**
     * 查询SysMediaType列表
     *
     * @param SysMediaType sysMediaType
     * @return SysMediaType集合
     */
    public List<SysMediaTypeEntity> selectSysMediaTypeList(SysMediaTypeEntity sysMediaTypeEntity);

    /**
     * 批量新增SysMediaTypeEntity
     *
     * @param sysMediaTypeEntityList
     * @return 结果
     */
    public int insertSysMediaTypeList(List<SysMediaTypeEntity> list);

    /**
     * 新增SysMediaTypeEntity
     *
     * @param sysMediaTypeEntity
     * @return 结果
     */
    public int insertSysMediaType(SysMediaTypeEntity sysMediaTypeEntity);

    /**
     * 批量修改SysMediaTypeEntity
     *
     * @param list
     * @return 结果
     */
    public int updateSysMediaType(List<SysMediaTypeEntity> list);

    /**
     * 修改SysMediaTypeEntity
     *
     * @param SysMediaTypeEntity
     * @return 结果
     */
    public int updateSysMediaType(SysMediaTypeEntity sysMediaTypeEntity);

}
