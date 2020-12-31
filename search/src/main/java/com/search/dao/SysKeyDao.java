package com.search.dao;

import com.search.entity.SysKeyEntity;

import java.util.List;
import java.util.Map;

/**
 * SysKeyDao接口
 *
 * @date 2020-12-22 00:02:33
 */
public interface SysKeyDao {
    /**
     * 查询SysKeyEntity
     *
     * @param sysKeyEntity
     * @return SysKeyEntity
     */
    public SysKeyEntity selectSysKeyOne(SysKeyEntity sysKeyEntity);

    /**
     * 查询SysKeyEntity列表
     *
     * @param sysKeyEntity
     * @return SysKeyEntity集合
     */
    public List<SysKeyEntity> selectSysKeyList(SysKeyEntity sysKeyEntity);

    /**
     * 新增SysKeyEntity
     *
     * @param sysKeyEntity
     * @return 结果
     */
    public int insertSysKey(List<SysKeyEntity> list);

    /**
     * 修改sysKey
     *
     * @param sysKeyEntity
     * @return 结果
     */
    public int updateSysKey(List<SysKeyEntity> list);

    /**
     * 查询前50个词频的名称
     * @return
     */
    public List<Map<String,Object>> selectKeyword(SysKeyEntity sysKeyEntity);
}
