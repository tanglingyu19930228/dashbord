package com.search.entity;


import java.io.Serializable;
import lombok.Data;

/**
 * 词频表
 * 
 * @author Administrator
 * @date 2020-12-22 00:02:33
 */
@Data
public class SysKeyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 数据id
	 */
	private Integer dataId;
	/**
	 * 单词
	 */
	private String keyWord;

}
