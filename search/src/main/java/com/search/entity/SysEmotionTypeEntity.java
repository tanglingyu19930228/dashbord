package com.search.entity;


import java.io.Serializable;
import lombok.Data;

/**
 * 情感表
 * 
 * @author Administrator
 * @date 2020-12-22 00:51:05
 */
@Data
public class SysEmotionTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 情感id
	 */
	private Integer emotionType;
	/**
	 * 情感名称
	 */
	private String emotionName;

}
