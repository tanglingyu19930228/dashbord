package com.search.common.page;

/**
 * 表格数据处理
 * 
 * @author admin
 */
public class TableSupport {
	/**
	 * 封装分页对象
	 */
	public static PageDomain getPageDomain() {
		return new PageDomain();
	}

	public static PageDomain buildPageRequest() {
		return getPageDomain();
	}
}
