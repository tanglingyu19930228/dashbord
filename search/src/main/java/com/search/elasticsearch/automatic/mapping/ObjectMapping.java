package com.search.elasticsearch.automatic.mapping;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.io.Serializable;

/**
 * Elastic Mapping Builder, XContentBuilder.
 *
 * @author frekele - Leandro Kersting de Freitas
 */
public class ObjectMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    private XContentBuilder mapping;

    public ObjectMapping(XContentBuilder mapping) {
        this.mapping = mapping;
    }

    public XContentBuilder getContent() throws IOException {
        return this.mapping;
    }

    public String getContentAsString() throws IOException {
        return Strings.toString(this.getContent());
    }

}
