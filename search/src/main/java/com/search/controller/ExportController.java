package com.search.controller;

import com.search.annotation.BizLog;
import com.search.biz.ExportService;
import com.search.common.utils.DateUtils;
import com.search.common.utils.ExcelUtils;
import com.search.common.utils.R;
import com.search.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 导出excel 控制类
 *
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/export")
@Slf4j
@Api(value = "导出excel 控制类", tags = "excel导出接口")
public class ExportController {

    /**
     * 站点名称
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/siteName")
    @BizLog(action = "站点名称导出")
    @ApiOperation(value = "站点名称导出")
    public void siteName(HttpServletResponse response, @RequestBody @Valid List<OverviewSiteExcel> overviewSiteExcels) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportSiteHeaders(overviewSiteExcels);
            String filePath = "D:/siteName.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("站点名称.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }

    /**
     * 声音来源分析(微博)
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/weiBoExport")
    @BizLog(action = "声音来源分析(微博)导出")
    @ApiOperation(value = "声音来源分析(微博)导出")
    public void weiBoExport(HttpServletResponse response, @RequestBody @Valid List<OverviewWeiBoAnalysis> overviewWeiBoAnalyses) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportWeiBoHeaders(overviewWeiBoAnalyses);
            String filePath = "D:/siteName.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("声音来源分析(微博).xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }

    /**
     * 内容高频词
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/contentHighWordExport")
    @BizLog(action = "内容高频词导出")
    @ApiOperation(value = "内容高频词导出")
    public void contentHighWordExport(HttpServletResponse response, @RequestBody @Valid List<OverviewContentHighWord> overviewContentHighWords) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportHighWordHeaders(overviewContentHighWords);
            String filePath = "D:/siteName.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("内容高频词.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }

    /**
     * 声量来源分析(新闻)
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/newsOriginExport")
    @BizLog(action = "声量来源分析(新闻)导出")
    @ApiOperation(value = "声量来源分析(新闻)导出")
    public void newsOriginExport(HttpServletResponse response, @RequestBody @Valid List<OverviewNewsOrigin> overviewNewsOrigins) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportNewOriginHeaders(overviewNewsOrigins);
            String filePath = "D:/siteName.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("声量来源分析(新闻).xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }

    /**
     * 声量来源分析(微信)
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/wsOriginExport")
    @BizLog(action = "声量来源分析(微信)导出")
    @ApiOperation(value = "声量来源分析(微信)导出")
    public void wsOriginExport(HttpServletResponse response, @RequestBody @Valid List<OverviewWsOrigin> overviewWsOrigins) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportWsOriginHeaders(overviewWsOrigins);
            String filePath = "D:/siteName.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("声量来源分析(微信).xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }


    /**
     * 声量来源
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/voiceOrigin")
    @BizLog(action = "声量来源导出")
    @ApiOperation(value = "声量来源导出")
    @ResponseBody
    public R voiceOrigin(HttpServletResponse response, @RequestBody @Valid List<OverviewVoiceOrigin> overviewVoiceOrigins) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportVoiceOriginHeaders(overviewVoiceOrigins);
            String filePath = "D:/voiceOrigin"+DateUtils.getNowDate()+".xls";
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
            return R.ok(filePath);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("服务器异常");
        }
    }
    @RequestMapping(value = "/overview/voiceOriginGet")
    public void voiceOriginGet(HttpServletResponse response,@RequestParam("filePath") String filePath) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("声量来源.xls", "UTF-8"));
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }

    /**
     * 声量趋势
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/exportVoiceTrend")
    @ResponseBody
    @BizLog(action = "声量趋势导出")
    @ApiOperation(value = "声量趋势导出")
    public R exportVoiceTrend(HttpServletResponse response, @RequestBody @Valid List<OverviewArticleTrendExcel> overviewArticleTrendExcels) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportVoiceTrendHeaders(overviewArticleTrendExcels);
            String filePath = "D:/exportVoiceTrend"+ DateUtils.getNowDate().getTime()+".xls";
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            return R.ok(filePath);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error();
        }
    }

    @RequestMapping(value = "/overview/exportVoiceTrendGet")
    public void exportVoiceTrendGet(HttpServletResponse response,@RequestParam("filePath") String filePath) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("声量趋势.xls", "UTF-8"));
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }


    /**
     * 行业话题趋势
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/topicTrendcy")
    @BizLog(action = "行业话题趋势导出")
    @ApiOperation(value = "行业话题趋势导出")
    public void topicTrendcy(HttpServletResponse response, @RequestBody @Valid List<OverviewTopicTrency> overviewTopicTrencies) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportTopicTrendHeaders(overviewTopicTrencies);
            String filePath = "D:/a.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("行业话题趋势.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }


    /**
     * 情感度趋势
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/emmotionTrendcy")
    @BizLog(action = "情感度趋势导出")
    @ApiOperation(value = "情感度趋势导出")
    public void emmotionTrendcy(HttpServletResponse response, @RequestBody @Valid List<OverviewEmmotionTrency> overviewEmmotionTrencies) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportEmmotaionTrendHeaders(overviewEmmotionTrencies);
            String filePath = "D:/a.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("情感度趋势.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }


    /**
     * 情感度占比
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/emmotionPercent")
    @BizLog(action = "情感度占比导出")
    @ApiOperation(value = "情感度占比导出")
    public void emmotionPercent(HttpServletResponse response, @RequestBody @Valid List<OverviewEmmotionPercent> overviewEmmotionPercents) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportEmmotaionPercentHeaders(overviewEmmotionPercents);
            String filePath = "D:/a.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("情感度占比.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }


    /**
     * 行业话题占比
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/tradePercent")
    @BizLog(action = "行业话题占比导出")
    @ApiOperation(value = "行业话题占比导出")
    public void tradePercent(HttpServletResponse response, @RequestBody @Valid List<OverviewTradePercent> overviewTradePercents) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportTradePercentHeaders(overviewTradePercents);
            String filePath = "D:/a.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("行业话题占比.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }


    /**
     * 品牌产品声量趋势对比
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/compareProduct")
    @BizLog(action = "品牌产品声量趋势对比导出")
    @ApiOperation(value = "品牌产品声量趋势对比导出")
    public void compareProduct(HttpServletResponse response, @RequestBody @Valid List<OverviewCompareProduct> overviewCompareProducts) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportCompareProductHeaders(overviewCompareProducts);
            String filePath = "D:/a.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("品牌产品声量趋势对比.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }


    /**
     * 品牌产品总声量对比
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/compareProductTotal")
    @BizLog(action = "品牌产品总声量对比导出")
    @ApiOperation(value = "品牌产品总声量对比导出")
    public void compareProductTotal(HttpServletResponse response, @RequestBody @Valid List<OverviewCompareProductCom> overviewCompareProductComs) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportCompareProductComHeaders(overviewCompareProductComs);
            String filePath = "D:/a.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("品牌产品总声量对比.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }


    /**
     * 品牌产品互动量趋势对比
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/compareProductCom")
    @BizLog(action = "品牌产品互动量趋势对比导出")
    @ApiOperation(value = "品牌产品互动量趋势对比导出")
    public void compareProductCom(HttpServletResponse response, @RequestBody @Valid List<OverviewCompareProductTotal> overviewCompareProductTotals) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportCompareProductTotalHeaders(overviewCompareProductTotals);
            String filePath = "D:/compareProductTotal.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("品牌产品互动量趋势对比.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }

    /**
     * 品牌产品总互动量对比
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/compareProductTotalCom")
    @BizLog(action = "品牌产品总互动量对比导出")
    @ApiOperation(value = "品牌产品总互动量对比导出")
    public void compareProductTotalCom(HttpServletResponse response, @RequestBody @Valid List<OverviewCompareProductTotalCom> overviewCompareProductTotalComs) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportCompareProductTotalComHeaders(overviewCompareProductTotalComs);
            String filePath = "D:/compareProductTotalCom.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("品牌产品总互动量对比.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }
}
