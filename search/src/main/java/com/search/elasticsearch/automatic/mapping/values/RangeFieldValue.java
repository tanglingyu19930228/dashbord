package com.search.elasticsearch.automatic.mapping.values;


import com.search.elasticsearch.automatic.mapping.annotations.values.BoolValue;
import com.search.elasticsearch.automatic.mapping.annotations.values.FloatValue;
import com.search.elasticsearch.automatic.mapping.enums.FieldType;

/**
 * @author frekele - Leandro Kersting de Freitas
 */
public class RangeFieldValue {

    private FieldType type;

    private String suffixName;

    private BoolValue coerce;

    private FloatValue boost;

    private BoolValue includeInAll;

    private BoolValue index;

    private BoolValue store;

    public RangeFieldValue(FieldType type, String suffixName, BoolValue coerce, FloatValue boost,
                           BoolValue includeInAll, BoolValue index, BoolValue store) {
        this.type = type;
        this.suffixName = suffixName;
        this.coerce = coerce;
        this.boost = boost;
        this.includeInAll = includeInAll;
        this.index = index;
        this.store = store;
    }

    public FieldType getType() {
        return type;
    }

    public String getSuffixName() {
        return suffixName;
    }

    public BoolValue getCoerce() {
        return coerce;
    }

    public FloatValue getBoost() {
        return boost;
    }

    public BoolValue getIncludeInAll() {
        return includeInAll;
    }

    public BoolValue getIndex() {
        return index;
    }

    public BoolValue getStore() {
        return store;
    }
}
