package com.poly.restaurant.application.port.out.repo;

import java.util.List;
import java.util.Optional;

public interface RepositoryPort<T, ID> {
    T save(T t);

    Optional<T> findById(ID id);

    List<T> findAll();

    boolean existsById(ID id);

    void deleteById(ID id);
}
