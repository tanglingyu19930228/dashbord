package com.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class DayCount {

    private Long dateKey;

    private Long docCount ;

    private Long total;

    private List<SiteNameVO> list;

    private Long totalPercent;

}
