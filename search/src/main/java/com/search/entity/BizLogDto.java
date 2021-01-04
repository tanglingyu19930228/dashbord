package com.search.entity;


import lombok.Data;

import java.util.Date;

@Data
public class BizLogDto {
    private Long id;
    private String loginIp;
    private String action;
    private String userName;
    private Integer  creatorId;
    private Date gmtCreate;
    private Date gmtModified;
}
