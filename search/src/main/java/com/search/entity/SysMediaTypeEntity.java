package com.search.entity;


import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 媒体表
 * 
 * @author Administrator
 * @date 2020-12-20 11:36:10
 */
@Data
public class SysMediaTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 媒体id
	 */
	private Integer mediaType;
	/**
	 * 媒体名称
	 */
	private String mediaName;

}
