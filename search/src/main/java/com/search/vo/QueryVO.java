package com.search.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class QueryVO {

    private Integer titleId;

    private Date startDate;

    private Date endDate;

    private Integer from;

    private List<Integer> emotionList;

    private List<Integer> mediaList;

    private List<Integer> topicList;

    private List<Integer> contentList;

    private List<String> includeList;

    private List<String> excludeList;

    private Integer pageSize;

    private Integer pageNumber;

    /**
     * 0:按照 时间查询
     * 1：按照 声量排序；
     */
    private Integer orderBy;

}
