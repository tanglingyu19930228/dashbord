package com.search.common.domain;

import lombok.Data;

@Data
public class BusinessResponse {
    private int code;
    private String msg;
    private Object data;
}
