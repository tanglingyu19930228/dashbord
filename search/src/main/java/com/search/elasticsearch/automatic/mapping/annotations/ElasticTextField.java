package com.search.elasticsearch.automatic.mapping.annotations;


import com.search.elasticsearch.automatic.mapping.annotations.values.BoolValue;
import com.search.elasticsearch.automatic.mapping.annotations.values.FielddataFrequencyFilterValue;
import com.search.elasticsearch.automatic.mapping.annotations.values.FloatValue;
import com.search.elasticsearch.automatic.mapping.annotations.values.IntValue;
import com.search.elasticsearch.automatic.mapping.enums.FieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A field to index full-text values, such as the body of an email or the description of a product.
 *
 * @author frekele - Leandro Kersting de Freitas
 * @see <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/text.html">Site Elasticsearch Reference Guide.</a>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticTextField {

    FieldType type = FieldType.TEXT;

    String suffixName() default "text";

    String analyzer() default "";

    @Deprecated
    FloatValue boost() default @FloatValue(ignore = true);

    BoolValue eagerGlobalOrdinals() default @BoolValue(ignore = true);

    BoolValue fielddata() default @BoolValue(ignore = true);

    FielddataFrequencyFilterValue fielddataFrequencyFilter() default @FielddataFrequencyFilterValue(
        ignore = true,
        min = @FloatValue(ignore = true),
        max = @FloatValue(ignore = true),
        minSegmentSize = @IntValue(ignore = true)
    );

    BoolValue includeInAll() default @BoolValue(ignore = true);

    BoolValue index() default @BoolValue(ignore = true);

    String indexOptions() default "";

    BoolValue norms() default @BoolValue(ignore = true);

    IntValue positionIncrementGap() default @IntValue(ignore = true);

    BoolValue store() default @BoolValue(ignore = true);

    String searchAnalyzer() default "";

    String searchQuoteAnalyzer() default "";

    String similarity() default "";

    String termVector() default "";

    String[] copyTo() default {};

}
