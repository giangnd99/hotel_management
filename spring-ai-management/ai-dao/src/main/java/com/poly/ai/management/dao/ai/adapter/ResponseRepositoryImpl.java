package com.poly.ai.management.dao.ai.adapter;

import com.poly.ai.management.dao.ai.entity.ResponseEntity;
import com.poly.ai.management.dao.ai.mapper.ResponseMapper;
import com.poly.ai.management.dao.ai.repository.ResponseJPARepository;
import com.poly.ai.management.domain.entity.train.Response;
import com.poly.ai.management.domain.port.output.repository.ResponseRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ResponseRepositoryImpl implements ResponseRepository {

    private final ResponseJPARepository responseJPARepository;

    public ResponseRepositoryImpl(ResponseJPARepository responseJPARepository) {
        this.responseJPARepository = responseJPARepository;
    }

    @Override
    public Response save(Response response) {
        ResponseEntity entity = ResponseMapper.toJPA(response);
        ResponseEntity saved = responseJPARepository.save(entity);
        return ResponseMapper.toDomain(saved);
    }

    @Override
    public Optional<Response> findById(String responseId) {
        return responseJPARepository.findById(responseId)
                .map(ResponseMapper::toDomain);
    }
}
