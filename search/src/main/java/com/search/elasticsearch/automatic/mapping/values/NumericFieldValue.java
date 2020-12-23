package com.search.elasticsearch.automatic.mapping.values;


import com.search.elasticsearch.automatic.mapping.annotations.values.BoolValue;
import com.search.elasticsearch.automatic.mapping.annotations.values.FloatValue;
import com.search.elasticsearch.automatic.mapping.annotations.values.IntValue;
import com.search.elasticsearch.automatic.mapping.enums.FieldType;

/**
 * @author frekele - Leandro Kersting de Freitas
 */
public class NumericFieldValue {

    private FieldType type;

    private String suffixName;

    private BoolValue coerce;

    private FloatValue boost;

    private BoolValue docValues;

    private BoolValue ignoreMalformed;

    private BoolValue includeInAll;

    private BoolValue index;

    private String nullValue;

    private BoolValue store;

    private IntValue scalingFactor;

    public NumericFieldValue(FieldType type, String suffixName, BoolValue coerce, FloatValue boost,
                             BoolValue docValues, BoolValue ignoreMalformed, BoolValue includeInAll,
                             BoolValue index, String nullValue, BoolValue store, IntValue scalingFactor) {
        this.type = type;
        this.suffixName = suffixName;
        this.coerce = coerce;
        this.boost = boost;
        this.docValues = docValues;
        this.ignoreMalformed = ignoreMalformed;
        this.includeInAll = includeInAll;
        this.index = index;
        this.nullValue = nullValue;
        this.store = store;
        this.scalingFactor = scalingFactor;
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

    public BoolValue getDocValues() {
        return docValues;
    }

    public BoolValue getIgnoreMalformed() {
        return ignoreMalformed;
    }

    public BoolValue getIncludeInAll() {
        return includeInAll;
    }

    public BoolValue getIndex() {
        return index;
    }

    public String getNullValue() {
        return nullValue;
    }

    public BoolValue getStore() {
        return store;
    }

    public IntValue getScalingFactor() {
        return scalingFactor;
    }
}
