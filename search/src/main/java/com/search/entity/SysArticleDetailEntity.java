package com.search.entity;


import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 帖子明细表
 * 
 * @author Administrator
 * @date 2020-12-09 23:40:13
 */
@Data
public class SysArticleDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 发布人
	 */
	private Integer publisherId;
	/**
	 * 发布人
	 */
	private String publisher;
	/**
	 * 发布人id
	 */
	private String publisherUrl;
	/**
	 * 发布人id
	 */
	private Integer publisherUserType;
	/**
	 * 发布人时间
	 */
	private Date publisherTime;

}
