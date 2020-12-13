package com.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.search.common.utils.R;
import com.search.entity.SysArticleEntity;
import com.search.service.ISysArticleService;
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
 * @time: 2020/12/13 11:56
 */

@RestController("/sysArticle")
@Slf4j
@Api(value = "sys_article控制层", tags = "标题接口")
public class SysArticleController {

    @Resource
    private ISysArticleService iSysArticleService;

    /**
     * 根据id查询sys_article信息
     */
    @GetMapping("/selectSysArticleById/{id}")
    @ApiOperation("根据id查询sys_article信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "sys_article的id", dataType = "Integer", paramType = "Integer"))
    public R selectSysArticleById(@PathVariable(value = "id") @Valid Integer id) {
        log.info("根据id查询sys_article信息id={}", id);
        SysArticleEntity sysArticleEntity = iSysArticleService.selectSysArticleById(id);
        return sysArticleEntity == null ? R.ok("没有该记录") : R.ok(sysArticleEntity);
    }

    /**
     * sys_article信息查询
     *
     * @Return List
     */
    @RequestMapping("/selectSysArticleList")
    @ApiOperation("sys_article信息查询(多条记录)")
    public R selectSysArticleList(@RequestBody @Valid SysArticleEntity sysArticleEntity) {
        log.info("查询sys_article信息sysArticleEntity={}", JSONObject.toJSON(sysArticleEntity));
        List<SysArticleEntity> sysArticleEntities = iSysArticleService.selectSysArticleList(sysArticleEntity);
        return R.ok(sysArticleEntities);
    }

    /**
     * sys_article信息
     *
     * @Return singleton
     */
    @RequestMapping("/selectSysArticleSingleton")
    @ApiOperation("sys_article信息查询(单条记录)")
    public R selectSysArticleSingleton(@RequestBody @Valid SysArticleEntity sysArticleEntity) {
        log.info("单条记录查询sys_article信息sysArticleEntity={}", JSONObject.toJSON(sysArticleEntity));
        SysArticleEntity result = iSysArticleService.selectSysArticleOne(sysArticleEntity);
        return result == null ? R.ok("没有该记录") : R.ok(result);
    }

    /**
     * 新增SysArticleEntity
     */
    @RequestMapping("/addSysArticle")
    @ApiOperation("新增sys_article信息")
    public R addSysArticle(@RequestBody @Valid SysArticleEntity sysArticleEntity) {
        log.info("新增sys_article信息sysArticleEntity={}", JSONObject.toJSON(sysArticleEntity));
        int result = iSysArticleService.insertSysArticle(sysArticleEntity);
        return result == 1 ? R.ok() : R.error("新增sys_article信息失败");
    }

    /**
     * 批量新增SysArticleEntity
     */
    @RequestMapping("/batchAddSysArticle")
    @ApiOperation("批量新增sys_article信息")
    public R batchAddSysArticle(@RequestBody @Valid List<SysArticleEntity> sysArticleEntityList) {
        if (CollectionUtils.isEmpty(sysArticleEntityList)) {
            log.error("入参有误sysArticleEntityList={}", JSONObject.toJSON(sysArticleEntityList));
            return R.error("入参有误");
        }
        log.info("批量新增sysArticle");
        int addSize = sysArticleEntityList.size();
        int result = iSysArticleService.insertSysArticleList(sysArticleEntityList);
        return addSize == result ? R.ok("批量新增成功") : addSize > result ? R.ok("部分插入成功") : R.error("不可能出现此情况");
    }

    /**
     * 修改sys_article
     */
    @RequestMapping("/updateSysArticle")
    @ApiOperation("修改sys_article")
    public R updateSysArticle(@RequestBody @Valid SysArticleEntity sysArticleEntity) {
        log.info("修改sys_article信息sysArticleEntity={}", JSONObject.toJSON(sysArticleEntity));
        int result = iSysArticleService.updateSysArticle(sysArticleEntity);
        return result == 1 ? R.ok() : R.error("修改sys_article信息失败");
    }

    /**
     * 根据id批量更新sys_article
     */
    @RequestMapping("/batchUpdateSysArticle")
    @ApiOperation("批量修改sys_article")
    public R batchUpdateSysArticle(@RequestBody @Valid List<SysArticleEntity> sysArticleEntityList) {
        if (CollectionUtils.isEmpty(sysArticleEntityList)) {
            log.error("根据id批量更新sys_article入参有误sysArticleEntityList={}", JSONObject.toJSON(sysArticleEntityList));
            return R.error("入参有误");
        }
        List<SysArticleEntity> collect = sysArticleEntityList.parallelStream().filter(sysArticleEntity -> sysArticleEntity.getId() != null).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            log.error("请求数据有误,有误的数据collect={}", JSONObject.toJSON(collect));
            return R.error("入参有误");
        }
        int result = iSysArticleService.updateSysArticle(sysArticleEntityList);
        return result == 1 ? R.ok() : R.error("修改sys_article信息失败");
    }
}
