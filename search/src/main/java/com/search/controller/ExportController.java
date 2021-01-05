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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
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
    public R siteName(HttpServletResponse response, @RequestBody @Valid List<OverviewSiteExcel> overviewSiteExcels) {
        /*try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportSiteHeaders(overviewSiteExcels);
            String filePath = "D:/siteName.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("站点名称.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }*/
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "站点名_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportSiteHeaders(overviewSiteExcels);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
    public R weiBoExport(HttpServletResponse response, @RequestBody @Valid List<OverviewWeiBoAnalysis> overviewWeiBoAnalyses) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "声音来源分析(微博)_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportWeiBoHeaders(overviewWeiBoAnalyses);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
    public R contentHighWordExport(HttpServletResponse response, @RequestBody @Valid List<OverviewContentHighWord> overviewContentHighWords) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "内容高频词_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportHighWordHeaders(overviewContentHighWords);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
    public R newsOriginExport(HttpServletResponse response, @RequestBody @Valid List<OverviewNewsOrigin> overviewNewsOrigins) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "声量来源分析(新闻)_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportNewOriginHeaders(overviewNewsOrigins);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
    public R wsOriginExport(HttpServletResponse response, @RequestBody @Valid List<OverviewWsOrigin> overviewWsOrigins) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "声量来源分析(微信)_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportWsOriginHeaders(overviewWsOrigins);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "声量来源_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportVoiceOriginHeaders(overviewVoiceOrigins);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "声量趋势_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportVoiceTrendHeaders(overviewArticleTrendExcels);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
        }
    }

    @RequestMapping(value = "/overview/exportVoiceTrendGet")
    public void exportVoiceTrendGet(HttpServletResponse response, @RequestParam("filePath") String filePath) {
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
    public R topicTrendcy(HttpServletResponse response, @RequestBody @Valid List<OverviewTopicTrency> overviewTopicTrencies) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "行业话题趋势_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportTopicTrendHeaders(overviewTopicTrencies);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
    public R emmotionTrendcy(HttpServletResponse response, @RequestBody @Valid List<OverviewEmmotionTrency> overviewEmmotionTrencies) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "情感度趋势_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportEmmotaionTrendHeaders(overviewEmmotionTrencies);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
    public R emmotionPercent(HttpServletResponse response, @RequestBody @Valid List<OverviewEmmotionPercent> overviewEmmotionPercents) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "情感度占比_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportEmmotaionPercentHeaders(overviewEmmotionPercents);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
    public R tradePercent(HttpServletResponse response, @RequestBody @Valid List<OverviewTradePercent> overviewTradePercents) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "行业话题占比_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportTradePercentHeaders(overviewTradePercents);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
    public R compareProduct(HttpServletResponse response, @RequestBody @Valid List<OverviewCompareProduct> overviewCompareProducts) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "品牌产品声量趋势对比_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportCompareProductHeaders(overviewCompareProducts);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
    public R compareProductTotal(HttpServletResponse response, @RequestBody @Valid List<OverviewCompareProductCom> overviewCompareProductComs) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "品牌产品总声量对比_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportCompareProductComHeaders(overviewCompareProductComs);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
    public R compareProductCom(HttpServletResponse response, @RequestBody @Valid List<OverviewCompareProductTotal> overviewCompareProductTotals) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "品牌产品互动量趋势对比_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportCompareProductTotalHeaders(overviewCompareProductTotals);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
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
    public R compareProductTotalCom(HttpServletResponse response, @RequestBody @Valid List<OverviewCompareProductTotalCom> overviewCompareProductTotalComs) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            String fileName = "品牌产品总互动量对比_" + System.currentTimeMillis() + ".xls";
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportCompareProductTotalComHeaders(overviewCompareProductTotalComs);
            ExcelUtils.setExcelData(exportVoiceTrendHeaders, wb);
            ExcelUtils.setBrowser(response, wb, fileName);
            return R.ok();
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
            return R.error("导出异常");
        }
    }
}
