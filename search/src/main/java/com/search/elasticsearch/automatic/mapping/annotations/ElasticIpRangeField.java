package com.search.elasticsearch.automatic.mapping.annotations;


import com.search.elasticsearch.automatic.mapping.annotations.values.BoolValue;
import com.search.elasticsearch.automatic.mapping.annotations.values.FloatValue;
import com.search.elasticsearch.automatic.mapping.enums.FieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A range of ip values supporting either IPv4 or IPv6 (or mixed) addresses.
 *
 * @author frekele - Leandro Kersting de Freitas
 * @see <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/range.html">Site Elasticsearch Reference Guide.</a>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticIpRangeField {

    FieldType type = FieldType.IP_RANGE;

    String suffixName() default "ipRange";

    BoolValue coerce() default @BoolValue(ignore = true);

    @Deprecated
    FloatValue boost() default @FloatValue(ignore = true);

    BoolValue includeInAll() default @BoolValue(ignore = true);

    BoolValue index() default @BoolValue(ignore = true);

    BoolValue store() default @BoolValue(ignore = true);

}
