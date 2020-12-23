package com.search.elasticsearch.automatic.mapping.annotations;


import com.search.elasticsearch.automatic.mapping.annotations.values.BoolValue;
import com.search.elasticsearch.automatic.mapping.enums.FieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields of type geo_point accept latitude-longitude pairs, which can be used:
 * - to find geo-points within a bounding box, within a certain distance of a central point, or within a polygon.
 * - to aggregate documents by geographically or by distance from a central point.
 * - to integrate distance into a document’s relevance score.
 * - to sort documents by distance.
 *
 * @author frekele - Leandro Kersting de Freitas
 * @see <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-point.html">Site Elasticsearch Reference Guide.</a>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticGeoPointField {

    FieldType type = FieldType.GEO_POINT;

    String suffixName() default "geoPoint";

    BoolValue ignoreMalformed() default @BoolValue(ignore = true);

}
