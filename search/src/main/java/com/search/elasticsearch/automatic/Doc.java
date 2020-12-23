package com.search.elasticsearch.automatic;

import com.alibaba.fastjson.annotation.JSONField;
import com.search.elasticsearch.automatic.mapping.annotations.ElasticDateField;
import com.search.elasticsearch.automatic.mapping.annotations.ElasticIgnored;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * 描述：TODO
 * Author：linyue
 * Date：2020/4/10
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class Doc {
    @ElasticIgnored
    @JSONField(serialize = false)
    String id;
    @ElasticDateField
    OffsetDateTime gmtCreate;
    @ElasticDateField
    OffsetDateTime gmtModified;
}
