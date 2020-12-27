package com.search.elasticsearch.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.search.common.utils.R;
import com.search.elasticsearch.AbstractElasticsearchService;
import com.search.vo.QueryVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class AggElasticsearchService extends AbstractElasticsearchService {

    private RestHighLevelClient restHighLevelClient;

    private static final String INDEX_NAME = "jl";


    @Autowired
    public AggElasticsearchService(RestHighLevelClient restHighLevelClient) {
        super(restHighLevelClient);
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public String logicIndex() {
        return null;
    }

    public R aggBusinessQuery(QueryVO queryVO){
        return R.ok();
    }

}
