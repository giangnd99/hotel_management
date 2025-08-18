package com.poly.restaurant.application.handler.impl;

import com.poly.restaurant.application.port.out.repo.RepositoryPort;

import java.util.List;

public abstract class AbstractGenericHandlerImpl<T, ID> {

    protected abstract RepositoryPort<T, ID> getRepository();

    public T create(T t) {
        return getRepository().save(t);
    }

    public T update(ID id, T t) {
        if (!getRepository().existsById(id)) {
            throw new RuntimeException("Entity not found: " + id);
        }
        return getRepository().save(t);
    }

    public T getById(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found: " + id));
    }

    public List<T> getAll() {
        return getRepository().findAll();
    }

    public void delete(ID id) {
        getRepository().deleteById(id);
    }
}
