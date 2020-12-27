package com.search.enums;

/**
 * @author Administrator
 */
public enum SelectEnum {

    /** 话题 */
    TOPIC(0,"topic"),
    /** 媒体类型 */
    MEDIA_TYPE(1,"mediaType"),
    /** ugc pgc */
    CONTENT_TYPE(2,"contentType"),
    /** 情感类型 */
    EMOTION_TYPE(3,"emotionType");

    private final Integer  code;

    private final String name;

    SelectEnum(Integer code,String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private String getStringByCode(Integer code){
        for (SelectEnum one : values()) {
            if(code.equals(one.getCode())){
                return one.getName();
            }
        }
        return "";
    }

}
