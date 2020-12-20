package com.search.entity;


import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 话题表
 * 
 * @author Administrator
 * @date 2020-12-20 11:36:10
 */
@Data
public class SysTopicEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 话题id
	 */
	private Integer topicId;
	/**
	 * 话题名称
	 */
	private String topicName;

}
