package com.search.common.domain;

/**
 * @author tanglingyu
 */
public class BusinessException extends RuntimeException {

    private int code;
    private String msg;

    public BusinessException() {
        
    }

    public BusinessException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }


    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
