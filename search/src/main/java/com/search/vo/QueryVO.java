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

    private List<Integer> emotionList;

    private List<Integer> mediaList;

    private List<Integer> topicList;

    private List<Integer> contentList;

    private List<String> includeList;

    private List<String> excludeList;

}
