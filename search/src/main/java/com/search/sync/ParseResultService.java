package com.search.sync;

import com.alibaba.fastjson.JSONObject;
import com.search.biz.compare.BrandProductCompareService;
import com.search.biz.contain.ContainAnalysisService;
import com.search.biz.overview.OverViewService;
import com.search.common.utils.BigDecimalUtils;
import com.search.dao.SysKeyDao;
import com.search.vo.DayCount;
import com.search.vo.QueryVO;
import com.search.vo.SiteNameVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Slf4j
@Service
public class ParseResultService {


    @Autowired
    OverViewService overViewService;

    @Autowired
    ContainAnalysisService containAnalysisService;

    @Autowired
    BrandProductCompareService brandProductCompareService;


    public JSONObject parseElasticsearchResponse(SearchResponse search, QueryVO queryVO) {
        if(search.status() != RestStatus.OK){
            JSONObject object = new JSONObject();
            object.put("msg","查询异常");
            return object;
        }
        return this.parseElasticsearchResponseRouter(search,queryVO.getFrom());
    }

    private JSONObject parseElasticsearchResponseRouter(SearchResponse search, Integer from) {
        if(from ==0 ){
            return overViewService.parseDetailPageResponse(search);
        }else if(from==1){
            return  containAnalysisService.parseAnalysisPageResponse(search);
        }else{
            return brandProductCompareService.parseComparePageResponse(search);
        }
    }


}
