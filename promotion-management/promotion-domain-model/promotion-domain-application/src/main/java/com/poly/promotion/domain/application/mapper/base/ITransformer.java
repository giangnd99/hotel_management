package com.poly.promotion.domain.application.mapper.base;

@FunctionalInterface
public interface ITransformer<K, V> {
    V transform(K from);
}
