package com.poly.ai.management.dao.ai.adapter;

import com.poly.ai.management.dao.ai.repository.ResponseJPARepository;
import com.poly.ai.management.domain.entity.Response;
import com.poly.ai.management.domain.port.output.repository.ResponseRepository;
import org.springframework.stereotype.Component;

@Component
public class ResponseRepositoryImpl implements ResponseRepository {

    private final ResponseJPARepository responseJPARepository;

    public ResponseRepositoryImpl(ResponseJPARepository responseJPARepository) {
        this.responseJPARepository = responseJPARepository;
    }

    @Override
    public Response save(Response response) {
        return responseJPARepository.save(response);
    }
}
