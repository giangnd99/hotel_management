package com.poly.promotion.domain.application.mapper.base;

import java.util.List;

public interface ITransformer<K, V> {
    V transform(K from);
    default List<V> transformCollection(List<K> from) {
        return from.stream()
                .map(this::transform)
                .toList();
    }
}
