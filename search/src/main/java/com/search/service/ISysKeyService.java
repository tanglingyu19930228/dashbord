package com.search.service;

import com.search.entity.SysKeyEntity;

import java.util.List;

/**
 * sysKeyService接口
 *
 * @author chenshun
 * @date 2020-12-22 00:02:33
 */
public interface ISysKeyService {
    /**
     * 查询SysKeyEntity
     *
     * @param id
     * @return SysKeyEntity
     */
    public SysKeyEntity selectSysKeyById(Integer id);

    /**
     * 查询一个SysKeyEntity
     *
     * @param sysKeyEntity sysKeyEntity
     * @return SysKeyEntity
     */
    public SysKeyEntity selectSysKeyOne(SysKeyEntity sysKeyEntity);

    /**
     * 查询SysKey列表
     *
     * @param sysKeyEntity sysKeyEntity
     * @return SysKey集合
     */
    public List<SysKeyEntity> selectSysKeyList(SysKeyEntity sysKeyEntity);

    /**
     * 批量新增SysKeyEntity
     *
     * @param list list
     * @return 结果
     */
    public int insertSysKeyList(List<SysKeyEntity> list);

    /**
     * 新增SysKeyEntity
     *
     * @param sysKeyEntity sysKeyEntity
     * @return 结果
     */
    public int insertSysKey(SysKeyEntity sysKeyEntity);

    /**
     * 批量修改SysKeyEntity
     *
     * @param list list
     * @return 结果
     */
    public int updateSysKey(List<SysKeyEntity> list);

    /**
     * 修改SysKeyEntity
     *
     * @param sysKeyEntity sysKeyEntity
     * @return 结果
     */
    public int updateSysKey(SysKeyEntity sysKeyEntity);

}
