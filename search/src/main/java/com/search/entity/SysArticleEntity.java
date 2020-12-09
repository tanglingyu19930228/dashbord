package com.search.entity;


import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 角色表
 * 
 * @author Administrator
 * @date 2020-12-09 23:40:13
 */
@Data
public class SysArticleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 数据id(词频会与此关联)
	 */
	private Integer dataId;
	/**
	 * 标签id
	 */
	private Integer titleId;
	/**
	 * 话题id,逗号分隔
	 */
	private String topicId;
	/**
	 * 内容id(1:UGC;2:pgc)
	 */
	private Integer contentId;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 情感类型（0：中性 ; 1:负面；2：中性）
	 */
	private Integer emotionType;
	/**
	 * 情感类型（0：中性 ; 1:负面；2：中性）
	 */
	private Integer emotionValue;
	/**
	 * 插入时间
	 */
	private Date insertTime;
	/**
	 * 0：微信；1：微博；2：博客；3：论坛：4：问答；5：新闻
	 */
	private Integer mediaType;
	/**
	 * 发布人id
	 */
	private Integer publisherId;
	/**
	 * 点赞数
	 */
	private Integer publisherLinkNum;
	/**
	 * 阅读数
	 */
	private Integer publisherPv;
	/**
	 * 评论数
	 */
	private Integer publisherCommentNum;
	/**
	 * 收藏数
	 */
	private Integer publisherCollectionNum;
	/**
	 * 转发数
	 */
	private Integer publisherRepostNum;
	/**
	 * 站点名
	 */
	private String publisherSiteName;
	/**
	 * 文章头
	 */
	private String publisherArticleTitle;
	/**
	 * 文章base64
	 */
	private String publisherArticleUnique;
	/**
	 * 站点链接
	 */
	private String publisherArticleUrl;

}
