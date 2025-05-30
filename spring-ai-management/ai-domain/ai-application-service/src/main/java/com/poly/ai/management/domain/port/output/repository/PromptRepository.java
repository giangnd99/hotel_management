package com.poly.ai.management.domain.port.output.repository;


import com.poly.ai.management.domain.entity.Prompt;

public interface PromptRepository {

    Prompt save(Prompt prompt);
}
