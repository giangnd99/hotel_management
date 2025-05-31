package com.poly.ai.management.domain.port.output.repository;


import com.poly.ai.management.domain.entity.Response;

public interface ResponseRepository {

    Response save(Response response);
}
