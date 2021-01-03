package com.search.biz;

import com.search.vo.OverviewArticleTrendExcel;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<String> header2 = new ArrayList<>();
        header2.add("日期2");
        header2.add("声量2");
        header2.add("总声量2");
        list.add(header2);
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
}
