package com.search.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class ContainAnalysisVO {


    private Long docCount;

    private List<TopicAggVO> topicAggList;

    private List<EmotionAggVO> emotionAggList;

    private Date date;

    private Long dateLong;


}
