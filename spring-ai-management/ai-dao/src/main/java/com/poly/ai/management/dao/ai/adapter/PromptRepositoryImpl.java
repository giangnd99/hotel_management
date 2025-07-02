package com.poly.ai.management.dao.ai.adapter;

import com.poly.ai.management.dao.ai.entity.PromptEntity;
import com.poly.ai.management.dao.ai.mapper.PromptMapper;
import com.poly.ai.management.dao.ai.repository.PromptJPARepository;
import com.poly.ai.management.domain.entity.Prompt;
import com.poly.ai.management.domain.port.output.repository.PromptRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PromptRepositoryImpl implements PromptRepository {

    private final PromptJPARepository promptJPARepository;

    public PromptRepositoryImpl(PromptJPARepository promptJPARepository) {
        this.promptJPARepository = promptJPARepository;
    }

    @Override
    public Prompt save(Prompt prompt) {
        PromptEntity entity = PromptMapper.toJPA(prompt);
        PromptEntity saved = promptJPARepository.save(entity);
        return PromptMapper.toDomain(saved);
    }

    @Override
    public Optional<Prompt> findById(String promptId) {
        return promptJPARepository.findById(promptId)
                .map(PromptMapper::toDomain);
    }
}
