package com.poly.restaurant.application.handler;

import java.util.List;

public interface GenericHandler<T, ID> {
    T create(T t);

    T update(ID id, T t);

    T getById(ID id);

    List<T> getAll();

    void delete(ID id);
}
