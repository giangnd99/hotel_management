package com.poly.ai.management.domain.handler.ai;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessDocumentHandler {

    public List<Document> processData(String rawData) {
        // rawData là dữ liệu từ DB (ví dụ: JSON, XML, hoặc văn bản thuần)
        Document doc = new Document(rawData);
        TokenTextSplitter splitter = new TokenTextSplitter();
        return splitter.apply(List.of(doc));
    }
}
