package com.search.common.utils;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Administrator
 */
public class R implements Serializable {
	private static final long serialVersionUID = -8157613083634272196L;

	// 通用属性
	/**响应代码*/
	private int code;
	/** 信息*/
	private String msg;

	//分页使用的属性
	/** 数据*/
	private List<?> rows;
	/** 页码*/
	private Integer pageNum;
	/** 总数*/
	private Long total;

	/** 用户token权限属性 */
	private Long userId;
	private String token;
	private Long expire;

	/** 文件上传属性 */
	private String fileName;
	private String url;

	private Object data;

	public R() {
		this.code = 0;
		this.msg = "success";
	}

	public static R error() {
		return error(500, "未知异常，请联系管理员");
	}

	public static R error(String msg) {
		return error(500, msg);
	}

	public static R error(int code, String msg) {
		R r = new R();
		r.code = code;
		r.msg = msg;
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.msg = msg;
		return r;
	}

	public static R ok() {
		return new R();
	}

	public static R ok(Object data){
		final R r = new R();
		r.data = data;
		return r;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public Object getData() {
		return data;
	}

	public R setData(Object data) {
		this.data = data;
		return this;
	}

	public List<?> getRows() {
		return rows;
	}

	public R setRows(List<?> rows) {
		this.rows = rows;
		return this;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public R setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
		return this;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Long getTotal() {
		return total;
	}

	public R setTotal(Long total) {
		this.total = total;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public R setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getToken() {
		return token;
	}

	public R setToken(String token) {
		this.token = token;
		return this;
	}

	public Long getExpire() {
		return expire;
	}

	public R setExpire(Long expire) {
		this.expire = expire;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public R setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public R setUrl(String url) {
		this.url = url;
		return this;
	}

	@JsonIgnore
	public boolean isSuccess() {
		return this.code == 0;
	}

	public String toJson() {
		return JSON.toJSONString(this);
	}
}