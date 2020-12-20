package com.search.service;

import com.search.entity.SysTopicEntity;

import java.util.List;

/**
 * sysTopicService接口
 *
 * @author chenshun
 * @date 2020-12-20 11:36:10
 */
public interface ISysTopicService {
    /**
     * 查询SysTopicEntity
     *
     * @param SysTopicID
     * @return SysTopicEntity
     */
    public SysTopicEntity selectSysTopicById(Integer id);

    /**
     * 查询一个SysTopicEntity
     *
     * @param SysTopicEntity sysTopicEntity
     * @return SysTopicEntity
     */
    public SysTopicEntity selectSysTopicOne(SysTopicEntity sysTopicEntity);

    /**
     * 查询SysTopic列表
     *
     * @param SysTopic sysTopic
     * @return SysTopic集合
     */
    public List<SysTopicEntity> selectSysTopicList(SysTopicEntity sysTopicEntity);

    /**
     * 批量新增SysTopicEntity
     *
     * @param sysTopicEntityList
     * @return 结果
     */
    public int insertSysTopicList(List<SysTopicEntity> list);

    /**
     * 新增SysTopicEntity
     *
     * @param sysTopicEntity
     * @return 结果
     */
    public int insertSysTopic(SysTopicEntity sysTopicEntity);

    /**
     * 批量修改SysTopicEntity
     *
     * @param list
     * @return 结果
     */
    public int updateSysTopic(List<SysTopicEntity> list);

    /**
     * 修改SysTopicEntity
     *
     * @param SysTopicEntity
     * @return 结果
     */
    public int updateSysTopic(SysTopicEntity sysTopicEntity);

}
