package com.search.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色表
 *
 * @author Administrator
 * @date 2020-12-09 23:40:13
 */
@Data
@ApiModel(value = "sys_article对应的实体")
public class SysArticleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ApiModelProperty("标题id")
    private Integer id;
    /**
     * 数据id(词频会与此关联)
     */
    @ApiModelProperty("数据id(词频会与此关联)")
    private Integer dataId;
    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private String titleId;
    /**
     * 话题id,逗号分隔
     */
    @ApiModelProperty("话题id,逗号分隔")
    private String topicId;
    /**
     * 内容id(1:UGC;2:pgc)
     */
    @ApiModelProperty("内容id(1:UGC;2:pgc)")
    private Integer contentId;
    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;
    /**
     * 情感类型（0：中性 ; 1:负面；2：中性）
     */
    @ApiModelProperty("情感类型（0：中性 ; 1:负面；2：中性）")
    private Integer emotionType;
    /**
     * 情感类型（0：中性 ; 1:负面；2：中性）
     */
    @ApiModelProperty("情感类型（0：中性 ; 1:负面；2：中性）")
    private Integer emotionValue;
    /**
     * 插入时间
     */
    @ApiModelProperty("插入时间")
    private Date insertTime;
    /**
     * 0：微信；1：微博；2：博客；3：论坛：4：问答；5：新闻
     */
    @ApiModelProperty("0：微信；1：微博；2：博客；3：论坛：4：问答；5：新闻")
    private Integer mediaType;
    /**
     * 发布人id
     */
    @ApiModelProperty("发布人id")
    private String publisherId;
    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer publisherLinkNum;
    /**
     * 阅读数
     */
    @ApiModelProperty("阅读数")
    private Integer publisherPv;
    /**
     * 评论数
     */
    @ApiModelProperty("评论数")
    private Integer publisherCommentNum;
    /**
     * 收藏数
     */
    @ApiModelProperty("收藏数")
    private Integer publisherCollectionNum;
    /**
     * 转发数
     */
    @ApiModelProperty("转发数")
    private Integer publisherRepostNum;
    /**
     * 站点名
     */
    @ApiModelProperty("站点名")
    private String publisherSiteName;
    /**
     * 文章头
     */
    @ApiModelProperty("文章头")
    private String publisherArticleTitle;
    /**
     * 文章base64
     */
    @ApiModelProperty("文章base64")
    private String publisherArticleUnique;
    /**
     * 站点链接
     */
    @ApiModelProperty("站点链接")
    private String publisherArticleUrl;

    /**
     * 发布人
     */
    @ApiModelProperty("发布人")
    private String publisher;

    @ApiModelProperty("发布人url")
    private String publisherUrl;
    /**
     * 发布人类型
     */
    @ApiModelProperty("发布人类型")
    private int publisherUserType;

    /**
     * 发布人时间
     */
    @ApiModelProperty("发布人人时间格式=yyyy-MM-dd HH:mm:ss")
    private Date publisherTime;

    /**
     * 包含的过滤词
     */
    @ApiModelProperty("包含的过滤词")
    private String includeQueryString;

    /**
     * 不包含的过滤词
     */
    @ApiModelProperty("不包含的过滤词")
    private String excludeQueryString;

}
