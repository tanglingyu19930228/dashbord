package com.search.vo;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class OverviewEmmotionTrency {


    private String productName;
    private String dataKey;
    private Long rightTotal;
    private Long middleTotal;
    private Long badTotal;
    private String rightPercent;
    private String middlePercent;
    private String badPercent;
}
