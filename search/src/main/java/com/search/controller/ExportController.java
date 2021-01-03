package com.search.controller;

import com.search.biz.ExportService;
import com.search.common.utils.ExcelUtils;
import com.search.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class ExportController {


    // 概览页 ==================== start

    /**
     * 站点名称
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/siteName")
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
    public void voiceOrigin(HttpServletResponse response, @RequestBody @Valid List<OverviewVoiceOrigin> overviewVoiceOrigins) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportVoiceOriginHeaders(overviewVoiceOrigins);
            String filePath = "D:/siteName.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(" 声量来源.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
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
    public void exportVoiceTrend(HttpServletResponse response, @RequestBody @Valid List<OverviewArticleTrendExcel> overviewArticleTrendExcels) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportVoiceTrendHeaders(overviewArticleTrendExcels);
            String filePath = "D:/a.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("声量趋势.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
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
    @RequestMapping(value = "/overview/compareProductTotal")
    public void compareProductCom(HttpServletResponse response, @RequestBody @Valid List<OverviewCompareProductTotal> overviewCompareProductTotals) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportCompareProductTotalHeaders(overviewCompareProductTotals);
            String filePath = "D:/a.xls";
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
    public void compareProductTotalCom(HttpServletResponse response, @RequestBody @Valid List<OverviewCompareProductTotalCom> overviewCompareProductTotalComs) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportCompareProductTotalComHeaders(overviewCompareProductTotalComs);
            String filePath = "D:/a.xls";
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("品牌产品总互动量对比.xls", "UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders, filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)), outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：", e);
        }
    }

    /**
     * 导出声量来源
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/exportVoiceResource")
    public void exportVoiceResource(HttpServletResponse response, @RequestBody @Valid List<OverviewArticleResourceExcel> overviewArticleResourceExcelList) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            String[] headers = ExportService.getExportVoiceResource();
//            ExcelUtils.exportExcel("name",headers,overviewArticleResourceExcelList,outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 声量来源分析
     * TODO
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/exportVoiceResourceAnalysis")
    public void exportVoiceResourceAnalysis(HttpServletResponse response) {

    }

    /**
     * 内容高频词  TODO  我自己生成
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/contentHighLevel")
    public void contentHighLevel(HttpServletResponse response, @RequestBody EmotionAggVO emotionAggVO) {


    }
    // 概览页 ==================== end

    // ================= 内容分析 start

    /**
     * 情感度占比
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/contain/emotionCompare")
    public void emotionCompare(HttpServletResponse response, @RequestBody List<ContainEmotionPercentExcel> containEmotionPercentExcelList) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            String[] headers = ExportService.getContainEmotionPercentExcelList();
//            ExcelUtils.exportExcel("name",headers,containEmotionPercentExcelList,outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 情感度趋势
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/contain/emotionTrend")
    public void emotionTrend(HttpServletResponse response) {
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            String[] headers = ExportService.getContainEmotionPercentExcelList();
//            ExcelUtils.exportExcel("name",headers,containEmotionPercentExcelList,outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 行业话题对比
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/contain/topicCompare")
    public void topicCompare(HttpServletResponse response) {

    }

    /**
     * 行业话题趋势
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/contain/topicTrend")
    public void topicTrend(HttpServletResponse response) {

    }
    // ================= 内容分析 end

    //  ================ 品牌/产品对标 start

    /**
     * 品牌/产品总声量对比
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/compare/totalArticle")
    public void totalArticle(HttpServletResponse response) {

    }

    /**
     * 品牌/产品声量趋势对比
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/compare/totalArticleTrend")
    public void totalArticleTrend(HttpServletResponse response) {

    }

    /**
     * 品牌/产品总互动量对比
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/compare/total")
    public void total(HttpServletResponse response) {

    }

    /**
     * 行业话题趋势
     *
     * @param response 返回excel
     */
    @RequestMapping(value = "/compare/totalTrend")
    public void totalTrend(HttpServletResponse response) {

    }

    //  ================ 品牌/产品对标 end
}
