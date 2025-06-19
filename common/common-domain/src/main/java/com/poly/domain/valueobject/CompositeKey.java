package com.poly.domain.valueobject;

import java.util.Objects;

public class CompositeKey<ID1, ID2> {
    private final ID1 id1;
    private final ID2 id2;

    public CompositeKey(ID1 id1, ID2 id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public ID1 getId1() {
        return id1;
    }

    public ID2 getId2() {
        return id2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey<ID1,ID2> that = (CompositeKey<ID1,ID2>) o;
        return Objects.equals(id1, that.id1) &&
                Objects.equals(id2, that.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }
}