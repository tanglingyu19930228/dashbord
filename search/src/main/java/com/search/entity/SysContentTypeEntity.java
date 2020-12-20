package com.search.entity;


import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 内容表
 * 
 * @author Administrator
 * @date 2020-12-20 11:36:10
 */
@Data
public class SysContentTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 内容id
	 */
	private Integer contentType;
	/**
	 * 内容名称
	 */
	private String contentName;

}
