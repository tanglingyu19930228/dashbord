package com.search.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 * @param <T>
 */
@Data
public class PageData<T> {

    /**
     * 页码，从1开始
     */
    @ApiModelProperty(value = "当前页码", required = true)
    public Integer pageNum;
    /**
     *页面大小
     */
    @ApiModelProperty(value = "分页大小", required = true)
    public Integer pageSize;
    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总数", required = true)
    public Long total;
    public List<T> list;

    public PageData() {
        this.pageNum = 0;
        this.pageSize = 0;
        this.total = 0L;
        this.list = new ArrayList<>();
    }

    public PageData(Integer page, Integer pageSize, Long total, List<T> data) {
        this.pageNum = page;
        this.pageSize = pageSize;
        this.total = total;
        this.list = data;
    }


}
