package com.poly.paymentdomain.output;

import java.util.List;
import java.util.Optional;

public interface RepositoryGeneric <OBJECT, ID>{
    // CRUD BASIC
    OBJECT save(OBJECT object);
    OBJECT update(OBJECT object);
    void delete(ID id);
    Optional<OBJECT> findById(ID id);
    List<OBJECT> findAll();

    List<OBJECT> findAllById(ID id);
    OBJECT remove(OBJECT id);

}
