package com.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.search.common.utils.R;
import com.search.entity.SysArticleDetailEntity;
import com.search.service.ISysArticleDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Tanglingyu
 * @time: 2020/12/13 12:31
 */
@RestController("/sysArticleDetail")
@Api(value = "sys_article_detail控制层", tags = "标题详情接口")
@Slf4j
public class SysArticleDetailController {

    @Resource
    private ISysArticleDetailService iSysArticleDetailService;

    /**
     * 根据id查询sys_article_detail信息
     */
    @GetMapping("/selectSysArticleDetailById/{id}")
    @ApiOperation("根据发布人publisherId查询sys_article_detail信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "sys_article_detail的publisherId", dataType = "Integer", paramType = "Integer"))
    public R selectSysArticleDetailById(@PathVariable(value = "id") @Valid Integer id) {
        log.info("根据id查询sys_article_detail信息id={}", id);
        SysArticleDetailEntity sysArticleDetailEntity = iSysArticleDetailService.selectSysArticleDetailById(id);
        return sysArticleDetailEntity == null ? R.ok("没有该记录") : R.ok(sysArticleDetailEntity);
    }

    /**
     * sys_article_detail信息查询
     *
     * @Return List
     */
    @RequestMapping("/selectSysArticleDetailList")
    @ApiOperation("sys_article_detail信息查询(多条记录)")
    public R selectSysArticleDetailList(@RequestBody @Valid SysArticleDetailEntity sysArticleDetailEntity) {
        log.info("查询sys_article_detail信息sysArticleDetailEntity={}", JSONObject.toJSON(sysArticleDetailEntity));
        List<SysArticleDetailEntity> sysArticleDetailEntities = iSysArticleDetailService.selectSysArticleDetailList(sysArticleDetailEntity);
        return R.ok(sysArticleDetailEntities);
    }

    /**
     * sys_article_detail信息
     *
     * @Return singleton
     */
    @RequestMapping("/selectSysArticleDetailSingleton")
    @ApiOperation("sys_article_detail信息查询(单条记录)")
    public R selectSysArticleDetailSingleton(@RequestBody @Valid SysArticleDetailEntity sysArticleDetailEntity) {
        log.info("单条记录查询sys_article_detail信息sysArticleDetailEntity={}", JSONObject.toJSON(sysArticleDetailEntity));
        SysArticleDetailEntity result = iSysArticleDetailService.selectSysArticleDetailOne(sysArticleDetailEntity);
        return result == null ? R.ok("没有该记录") : R.ok(result);
    }

    /**
     * 新增SysArticleDetailEntity
     */
    @RequestMapping("/addSysArticleDetail")
    @ApiOperation("新增SysArticleDetailEntity")
    public R addSysArticleDetail(@RequestBody @Valid SysArticleDetailEntity sysArticleDetailEntity) {
        log.info("新增sys_article_detail信息sysArticleDetailEntity={}", JSONObject.toJSON(sysArticleDetailEntity));
        int result = iSysArticleDetailService.insertSysArticleDetail(sysArticleDetailEntity);
        return result == 1 ? R.ok() : R.error("新增sys_article_detail信息失败");
    }

    /**
     * 批量新增SysArticleDetailEntity
     */
    @RequestMapping("/batchAddSysArticleDetail")
    @ApiOperation("批量新增SysArticleDetailEntity")
    public R batchAddSysArticleDetail(@RequestBody @Valid List<SysArticleDetailEntity> sysArticleDetailEntities) {
        if (CollectionUtils.isEmpty(sysArticleDetailEntities)) {
            log.error("入参有误sysArticleDetailEntities={}", JSONObject.toJSON(sysArticleDetailEntities));
            return R.error("入参有误");
        }
        log.info("批量新增sysArticleDetail");
        int addSize = sysArticleDetailEntities.size();
        int result = iSysArticleDetailService.insertSysArticleDetailList(sysArticleDetailEntities);
        return addSize == result ? R.ok("批量新增成功") : addSize > result ? R.ok("部分插入成功") : R.error("不可能出现此情况");
    }

    /**
     * 修改sys_article_detail
     */
    @RequestMapping("/updateSysArticleDetail")
    @ApiOperation("修改sys_article_detail")
    public R updateSysArticleDetail(@RequestBody @Valid SysArticleDetailEntity sysArticleDetailEntity) {
        log.info("修改sys_article_detail信息sysArticleDetailEntity={}", JSONObject.toJSON(sysArticleDetailEntity));
        int result = iSysArticleDetailService.updateSysArticleDetail(sysArticleDetailEntity);
        return result == 1 ? R.ok() : R.error("修改sys_article_detail信息失败");
    }

    /**
     * 根据id批量更新sys_article_detail
     */
    @RequestMapping("/batchUpdateSysArticleDetail")
    @ApiOperation("根据id批量更新sys_article_detail")
    public R batchUpdateSysArticleDetail(@RequestBody @Valid List<SysArticleDetailEntity> sysArticleDetailEntities) {
        if (CollectionUtils.isEmpty(sysArticleDetailEntities)) {
            log.error("根据id批量更新sys_article_detail入参有误sysArticleDetailEntities={}", JSONObject.toJSON(sysArticleDetailEntities));
            return R.error("入参有误");
        }
        List<SysArticleDetailEntity> collect = sysArticleDetailEntities.parallelStream().filter(str -> str.getPublisherId() != null).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            log.error("请求数据有误,有误的数据collect={}", JSONObject.toJSON(collect));
            return R.error("入参有误");
        }
        int result = iSysArticleDetailService.updateSysArticleDetail(sysArticleDetailEntities);
        return result == 1 ? R.ok() : R.error("修改sys_article信息失败");
    }
}
