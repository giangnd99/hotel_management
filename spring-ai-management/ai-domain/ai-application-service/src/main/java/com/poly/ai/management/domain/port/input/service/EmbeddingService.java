package com.poly.ai.management.domain.port.input.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface EmbeddingService {
    List<float[]> embedDocuments(List<Document> documents);

    float[] embedDocument(Document document);
}
