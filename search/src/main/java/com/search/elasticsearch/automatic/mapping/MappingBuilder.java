package com.search.elasticsearch.automatic.mapping;

import java.io.Serializable;

/**
 * Elastic Mapping Builder, Build Entity to Json Object.
 *
 * @author frekele - Leandro Kersting de Freitas
 */
public interface MappingBuilder extends Serializable {

    public ObjectMapping build(Class documentClass);

    public ObjectMapping build(boolean pretty, Class documentClass);
}
