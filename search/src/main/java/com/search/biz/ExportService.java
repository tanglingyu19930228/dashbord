package com.search.biz;

import com.search.common.utils.DateUtils;
import com.search.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class ExportService {


    public static List<List<String>> getExportVoiceTrendHeaders(List<OverviewArticleTrendExcel> articleTrendExcelList) {
        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("日期");
        header.add("声量");
        header.add("总声量");
        list.add(header);

        articleTrendExcelList.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getProductName());
            values.add(DateUtils.parseDateToStr("yyyy-MM-dd", new Date(entity.getDateKey())));
            values.add(String.valueOf(entity.getTotal()));
            list.add(values);
        });
        return list;
    }

    public static List<List<String>> getExportSiteHeaders(List<OverviewSiteExcel> overviewSiteExcels) {
        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("站点名称");
        header.add("声量");
        list.add(header);

        overviewSiteExcels.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getSiteName());
            values.add(String.valueOf(entity.getTotal()));
            list.add(values);
        });
        return list;
    }


    public static List<List<String>> getExportWeiBoHeaders(List<OverviewWeiBoAnalysis> overviewWeiBoAnalyses) {
        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("账号");
        header.add("头像url");
        header.add("声量");
        header.add("互动量");
        header.add("账号分类");
        header.add("关注数");
        header.add("粉丝数");
        list.add(header);

        overviewWeiBoAnalyses.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getAccountNo());
            values.add(entity.getUrl());
            values.add(String.valueOf(entity.getTotal()));
            values.add(String.valueOf(entity.getCommunication()));
            values.add(String.valueOf(entity.getAttention()));
            values.add(String.valueOf(entity.getFans()));
            list.add(values);
        });
        return list;

    }


    public static String[] getExportVoiceResource() {
        List<String> list = new ArrayList<>();
        list.add("媒体类型");
        list.add("声量");
        list.add("总声量");
        list.add("声量百分比");
        list.add("总声量百分比");
        return list.toArray(new String[0]);
    }

    public static String[] getContainEmotionPercentExcelList() {
        List<String> list = new ArrayList<>();
        list.add("情感类型");
        list.add("情感占比百分比");
        return list.toArray(new String[0]);
    }

    public static List<List<String>> getExportHighWordHeaders(List<OverviewContentHighWord> overviewContentHighWords) {
        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("高频词");
        header.add("数量");
        list.add(header);

        overviewContentHighWords.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getHighWord());
            values.add(String.valueOf(entity.getTotal()));
            list.add(values);
        });
        return list;

    }

    public static List<List<String>> getExportNewOriginHeaders(List<OverviewNewsOrigin> overviewNewsOrigins) {

        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("站点名称");
        header.add("声量");
        list.add(header);

        overviewNewsOrigins.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getSiteName());
            values.add(String.valueOf(entity.getTotal()));
            list.add(values);
        });
        return list;
    }

    public static List<List<String>> getExportWsOriginHeaders(List<OverviewWsOrigin> overviewWsOrigins) {
        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("账号");
        header.add("头像Url");
        header.add("声量");
        header.add("互动量");
        list.add(header);

        overviewWsOrigins.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getAccountNo());
            values.add(entity.getUrl());
            values.add(String.valueOf(entity.getTotal()));
            values.add(String.valueOf(entity.getCommunication()));
            list.add(values);
        });
        return list;
    }

    public static List<List<String>> getExportVoiceOriginHeaders(List<OverviewVoiceOrigin> overviewVoiceOrigins) {
        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("数据源");
        header.add("声量");
        header.add("占比");
        list.add(header);

        overviewVoiceOrigins.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getDataOrigin());
            values.add(String.valueOf(entity.getTotal()));
            values.add(String.valueOf(entity.getPercent()));
            list.add(values);
        });
        return list;
    }

    public static List<List<String>> getExportTopicTrendHeaders(List<OverviewTopicTrency> overviewTopicTrencies) {
        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();

        header.add("话题名称");
        header.add("日期");
        header.add("声量");
        list.add(header);

        overviewTopicTrencies.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getTopicName());
            values.add(String.valueOf(entity.getDataKey()));
            values.add(String.valueOf(entity.getTotal()));
            list.add(values);
        });
        return list;
    }

    public static List<List<String>> getExportEmmotaionTrendHeaders(List<OverviewEmmotionTrency> overviewEmmotionTrencies) {
        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("品牌/产品名称");
        header.add("日期");
        header.add("正面声量");
        header.add("中性声量");
        header.add("负面声量");
        header.add("正面占比");
        header.add("中性占比");
        header.add("负面占比");
        list.add(header);

        overviewEmmotionTrencies.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getProductName());
            values.add(String.valueOf(entity.getDataKey()));
            values.add(String.valueOf(entity.getRightTotal()));
            values.add(String.valueOf(entity.getMiddleTotal()));
            values.add(String.valueOf(entity.getBadTotal()));

            values.add(entity.getRightPercent());
            values.add(entity.getMiddlePercent());
            values.add(entity.getBadPercent());
            list.add(values);
        });
        return list;
    }

    public static List<List<String>> getExportEmmotaionPercentHeaders(List<OverviewEmmotionPercent> overviewEmmotionPercents) {

        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("品牌/产品名称");
        header.add("正面声量");
        header.add("中性声量");
        header.add("负面声量");
        list.add(header);

        overviewEmmotionPercents.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getProductName());
            values.add(String.valueOf(entity.getRightTotal()));
            values.add(String.valueOf(entity.getMiddleTotal()));
            values.add(String.valueOf(entity.getBadTotal()));
            list.add(values);
        });
        return list;

    }

    public static List<List<String>> getExportTradePercentHeaders(List<OverviewTradePercent> overviewTradePercents) {

        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("话题名称");
        header.add("声量");
        header.add("占比");
        list.add(header);

        overviewTradePercents.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getTopicName());
            values.add(String.valueOf(entity.getTotal()));
            values.add(entity.getPercent());
            list.add(values);
        });
        return list;

    }

    public static List<List<String>> getExportCompareProductHeaders(List<OverviewCompareProduct> overviewCompareProducts) {

        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("品牌/产品名称");
        header.add("日期");
        header.add("声量");
        list.add(header);

        overviewCompareProducts.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getProductName());
            values.add(entity.getDateKey());
            values.add(String.valueOf(entity.getTatal()));
            list.add(values);
        });
        return list;
    }

    public static List<List<String>> getExportCompareProductComHeaders(List<OverviewCompareProductCom> overviewCompareProductComs) {

        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("品牌/产品名称");
        header.add("日期");
        header.add("互动量");
        list.add(header);

        overviewCompareProductComs.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getProductName());
            values.add(entity.getDateKey());
            values.add(String.valueOf(entity.getTatal()));
            list.add(values);
        });
        return list;
    }

    public static List<List<String>> getExportCompareProductTotalHeaders(List<OverviewCompareProductTotal> overviewCompareProductTotals) {

        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("品牌/产品名称");
        header.add("总声量");
        list.add(header);

        overviewCompareProductTotals.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getProductName());
            values.add(String.valueOf(entity.getTotal()));
            list.add(values);
        });
        return list;
    }

    public static List<List<String>> getExportCompareProductTotalComHeaders(List<OverviewCompareProductTotalCom> overviewCompareProductTotalComs) {

        List<List<String>> list = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("品牌/产品名称");
        header.add("总互动量");
        list.add(header);

        overviewCompareProductTotalComs.stream().forEach(entity -> {
            List<String> values = new ArrayList<>();
            values.add(entity.getProductName());
            values.add(String.valueOf(entity.getTotal()));
            list.add(values);
        });
        return list;
    }
}
