package com.search.elasticsearch.automatic.mapping.annotations;

import com.search.elasticsearch.automatic.mapping.annotations.values.BoolValue;
import com.search.elasticsearch.automatic.mapping.annotations.values.FloatValue;
import com.search.elasticsearch.automatic.mapping.enums.FieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The geo_shape datatype facilitates the indexing of and searching with arbitrary geo shapes such as rectangles and polygons.
 * It should be used when either the data being indexed or the queries being executed contain shapes other than just points.
 *
 * @author frekele - Leandro Kersting de Freitas
 * @see <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-shape.html">Site Elasticsearch Reference Guide.</a>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticGeoShapeField {

    FieldType type = FieldType.GEO_SHAPE;

    String suffixName() default "geoShape";

    String tree() default "";

    String precision() default "";

    String treeLevels() default "";

    String strategy() default "";

    FloatValue distanceErrorPct() default @FloatValue(ignore = true);

    String orientation() default "";

    BoolValue pointsOnly() default @BoolValue(ignore = true);

}


