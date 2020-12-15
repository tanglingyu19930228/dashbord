package com.search.common.domain;

public enum BusinessResponseEnum {

    SUCCESS(0, "成功"),
    SYSTEM_ERROR(-1, "系统异常"),
    NON_LOGIN(100001, "未登录"),
    NOT_PERMITTED(100002, "无访问权限"),
    PARAM_INVALID(100003, "参数不合法"),
    CONVERSION_EXCEPTION(100004, "转化异常"),
    EXPORT_EXCEPTION(100005, "导出异常"),
    NEED_REDIRECT(100006, "需要重定向"),
    RECORD_NOT_EXISTS(100009, "未找到记录"),
    EXTRA(999999, "extra");

    private int code;
    private String msg;

    private BusinessResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
