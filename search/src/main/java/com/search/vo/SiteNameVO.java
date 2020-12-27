package com.search.vo;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class SiteNameVO {

    private Integer mediaType;

    private String name;

    private Long docCount;

    private Long publisherLinkNum;

    private Long publisherPv;

    private Long publisherCollectionNum;

    private Long publisherCommentNum;

    private Long publisherRepostNum;

}
