package com.search.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SumVoiceResp {

    private String date;
    private Long total;

    private Integer mediaType;

    private Long linkNumAll;

    private Long pvAll;

    private Long commentAll;

    private Long collectionAll;

    private Long  repostNum;





}
