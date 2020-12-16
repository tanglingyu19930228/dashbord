package com.search.common.domain;

import lombok.Data;

/**
 * @author tanglingyu
 */
@Data
public class BusinessResponse {
    private int code;
    private String msg;
    private Object data;
}
