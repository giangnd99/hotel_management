package com.poly.domain.valueobject;

public class CompositeId<ID1, ID2> extends BaseId<CompositeKey<ID1, ID2>> {
    protected CompositeId(CompositeKey<ID1, ID2> value) {
        super(value);
    }
}