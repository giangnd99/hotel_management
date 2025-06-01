package com.poly.ai.management.domain.port.output.repository;


import com.poly.ai.management.domain.entity.Prompt;

import java.util.Optional;

public interface PromptRepository {

    Prompt save(Prompt prompt);

    Optional<Prompt> findById(String promptId);
}
