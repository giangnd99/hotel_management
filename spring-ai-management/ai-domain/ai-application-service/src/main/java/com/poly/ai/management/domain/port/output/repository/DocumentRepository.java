package com.poly.ai.management.domain.port.output.repository;

import org.springframework.ai.document.Document;

import java.util.List;

public interface DocumentRepository {
    List<Document> findBySource(String source);
}
