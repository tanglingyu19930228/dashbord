package com.search.vo;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class OverviewWeiBoAnalysis {

    private String accountNo;
    private String url;
    private Long total;
    private Long communication;
    private String accountType;
    private Long attention;
    private Long fans;


}
