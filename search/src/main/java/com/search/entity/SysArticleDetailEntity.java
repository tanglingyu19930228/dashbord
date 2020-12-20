package com.search.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 帖子明细表
 *
 * @author Administrator
 * @date 2020-12-09 23:40:13
 */
@Data
@ApiModel(value = "帖子明细实体sys_article_detail")
public class SysArticleDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 发布人
     */
    @ApiModelProperty(name = "发布人id")
    private Integer publisherId;
    /**
     * 发布人
     */
    @ApiModelProperty(name = "发布人")
    private String publisher;
    /**
     * 发布人url
     */
    @ApiModelProperty(name = "发布人url")
    private String publisherUrl;
    /**
     * 发布人类型
     */
    @ApiModelProperty(name = "发布人类型")
    private Integer publisherUserType;
    /**
     * 发布人时间
     */
    @ApiModelProperty(name = "发布人时间")
    private Date publisherTime;

}
