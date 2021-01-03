package com.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.search.biz.ExportService;
import com.search.common.utils.ExcelUtils;
import com.search.vo.ContainEmotionPercentExcel;
import com.search.vo.OverviewArticleResourceExcel;
import com.search.vo.OverviewArticleTrendExcel;
import com.search.vo.EmotionAggVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 导出excel 控制类
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/export")
@Slf4j
public class ExportController {


    // 概览页 ==================== start

    /**
     * 声量趋势
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/exportVoiceTrend")
    public void exportVoiceTrend(HttpServletResponse response){
        try {
            final ServletOutputStream outputStream = response.getOutputStream();
            List<OverviewArticleTrendExcel> articleTrendExcelList = new ArrayList<>();
            final List<List<String>> exportVoiceTrendHeaders = ExportService.getExportVoiceTrendHeaders(articleTrendExcelList);
            String filePath = "D:/a.xls";
            response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("声量趋势.xls","UTF-8"));
            ExcelUtils.exportXSS(exportVoiceTrendHeaders,filePath);
            IOUtils.copy(new FileInputStream(new File(filePath)),outputStream);
        } catch (Exception e) {
            log.error("导出失败，异常信息为：",e);
        }
    }

    /**
     * 导出声量来源
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/exportVoiceResource")
    public void exportVoiceResource(HttpServletResponse response, @RequestBody List<OverviewArticleResourceExcel> overviewArticleResourceExcelList){
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
     * @param response 返回excel
     */
    @RequestMapping(value = "/overview/exportVoiceResourceAnalysis")
    public void exportVoiceResourceAnalysis(HttpServletResponse response){

    }

    /**
     * 内容高频词  TODO  我自己生成
     * @param response 返回excel
     */
    @RequestMapping(value = "/contentHighLevel")
    public void contentHighLevel(HttpServletResponse response, @RequestBody EmotionAggVO emotionAggVO){


    }
    // 概览页 ==================== end

    // ================= 内容分析 start

    /**
     * 情感度占比
     * @param response 返回excel
     */
    @RequestMapping(value = "/contain/emotionCompare")
    public void emotionCompare(HttpServletResponse response, @RequestBody List<ContainEmotionPercentExcel> containEmotionPercentExcelList){
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
     * @param response 返回excel
     */
    @RequestMapping(value = "/contain/emotionTrend")
    public void emotionTrend(HttpServletResponse response){
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
     * @param response 返回excel
     */
    @RequestMapping(value = "/contain/topicCompare")
    public void topicCompare(HttpServletResponse response){

    }
    /**
     * 行业话题趋势
     * @param response 返回excel
     */
    @RequestMapping(value = "/contain/topicTrend")
    public void topicTrend(HttpServletResponse response){

    }
    // ================= 内容分析 end

    //  ================ 品牌/产品对标 start

    /**
     * 品牌/产品总声量对比
     * @param response 返回excel
     */
    @RequestMapping(value = "/compare/totalArticle")
    public void totalArticle(HttpServletResponse response){

    }
    /**
     * 品牌/产品声量趋势对比
     * @param response 返回excel
     */
    @RequestMapping(value = "/compare/totalArticleTrend")
    public void totalArticleTrend(HttpServletResponse response){

    }
    /**
     * 品牌/产品总互动量对比
     * @param response 返回excel
     */
    @RequestMapping(value = "/compare/total")
    public void total(HttpServletResponse response){

    }
    /**
     * 行业话题趋势
     * @param response 返回excel
     */
    @RequestMapping(value = "/compare/totalTrend")
    public void totalTrend(HttpServletResponse response){

    }

    //  ================ 品牌/产品对标 end
}
