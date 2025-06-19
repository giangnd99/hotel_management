package com.poly.ai.management.domain.port.output.repository;

public interface DocumentRepository {
    List<DocumentEntity> findBySource(String source);
}
