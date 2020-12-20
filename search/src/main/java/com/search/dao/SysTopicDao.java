package com.search.dao;

import com.search.entity.SysTopicEntity;

import java.util.List;

/**
 * SysTopicDao接口
 *
 * @date 2020-12-20 11:36:10
 */
public interface SysTopicDao {
    /**
     * 查询SysTopicEntity
     *
     * @param sysTopicEntity
     * @return SysTopicEntity
     */
    public SysTopicEntity selectSysTopicOne(SysTopicEntity sysTopicEntity);

    /**
     * 查询SysTopicEntity列表
     *
     * @param sysTopicEntity
     * @return SysTopicEntity集合
     */
    public List<SysTopicEntity> selectSysTopicList(SysTopicEntity sysTopicEntity);

    /**
     * 新增SysTopicEntity
     *
     * @param sysTopicEntity
     * @return 结果
     */
    public int insertSysTopic(List<SysTopicEntity> list);

    /**
     * 修改sysTopic
     *
     * @param sysTopicEntity
     * @return 结果
     */
    public int updateSysTopic(List<SysTopicEntity> list);
}
