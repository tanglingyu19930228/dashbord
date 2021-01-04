package com.search.controller;

import com.search.annotation.BizLog;
import com.search.biz.SelectValueService;
import com.search.common.controller.BaseController;
import com.search.entity.SysTopicEntity;
import com.search.service.ISysTopicService;
import com.search.sync.ElasticsearchUtils;
import com.search.vo.QueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.search.common.utils.R;

import java.util.Objects;

/**
 * ${functionName} 提供者
 *
 * @author chenshun
 * @date 2020-12-20 11:36:10
 */
@RestController
@RequestMapping("/getSelect")
@Api(value = "SelectValueController", tags = "SelectValueController控制层")
public class SelectValueController extends BaseController {

    @Autowired
    SelectValueService selectValueService;

    @Autowired
    ElasticsearchUtils elasticsearchUtils;


    @PostMapping("/getAllData")
    @BizLog(action = "getAllData")
    @ApiOperation(value = "getAllData")
    public R getSelectValueService() {
        return selectValueService.getSelectValue();
    }

    @PostMapping("/getOverviewData")
    @BizLog(action = "getOverviewData")
    @ApiOperation(value = "getOverviewData")
    public R getOverviewData(@RequestBody QueryVO queryVO) {
        return elasticsearchUtils.doSingleSearch(queryVO, "newindex8");
    }

    @PostMapping("/getArticleShow")
    @BizLog(action = "getArticleShow")
    @ApiOperation(value = "getArticleShow")
    public R getArticleShow(@RequestBody QueryVO queryVO) {
        return elasticsearchUtils.justForContent(queryVO, "newindex8");
    }

    @PostMapping("/getStaticData")
    @BizLog(action = "getStaticData")
    @ApiOperation(value = "getStaticData")
    public R getStaticData() {
        return selectValueService.getStaticData();
    }

}
