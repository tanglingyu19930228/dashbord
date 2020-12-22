package com.search.dao;

import com.search.entity.SumVoiceResp;
import com.search.vo.QueryVO;

import java.util.List;
import java.util.Map;

/**
 * 概览页先使用此mapper
 * @author Administrator
 */
public interface SysOverviewCountDao {

    /**
     * 查询banner页接口
     * @param queryVO 查询条件
     * @return 返回结果
     */
    public List<SumVoiceResp> getOverviewBanner(QueryVO queryVO);

}
