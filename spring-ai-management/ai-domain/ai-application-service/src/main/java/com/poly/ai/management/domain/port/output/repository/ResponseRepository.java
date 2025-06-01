package com.poly.ai.management.domain.port.output.repository;


import com.poly.ai.management.domain.entity.Response;

import java.util.Optional;

public interface ResponseRepository {

    Response save(Response response);

    Optional<Response> findById(String responseId);
}
