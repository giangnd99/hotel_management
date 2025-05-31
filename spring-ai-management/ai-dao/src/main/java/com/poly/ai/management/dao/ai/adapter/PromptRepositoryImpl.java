package com.poly.ai.management.dao.ai.adapter;

import com.poly.ai.management.dao.ai.repository.PromptJPARepository;
import com.poly.ai.management.domain.entity.Prompt;
import com.poly.ai.management.domain.port.output.repository.PromptRepository;
import org.springframework.stereotype.Component;

@Component
public class PromptRepositoryImpl implements PromptRepository {

    private final PromptJPARepository promptJPARepository;
    public PromptRepositoryImpl(PromptJPARepository promptJPARepository) {
        this.promptJPARepository = promptJPARepository;
    }

    @Override
    public Prompt save(Prompt prompt) {
        return promptJPARepository.save(prompt);
    }
}
