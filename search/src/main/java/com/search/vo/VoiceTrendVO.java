package com.search.vo;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class VoiceTrendVO {

    private Integer mediaType;

    private String mediaName;

    private String siteName;

    private Long articleCount;

    private String publisher;

    private String publisherUrl;

    private Long sumAllCount;

    private Long publisherRepostNum;

    private Long publisherLinkNum;

    private Long publisherPv;

    private Long publisherCommentNum;

    private Long publisherCollectionNum;


}
