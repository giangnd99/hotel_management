package com.poly.ai.management.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiModelIntegrationTest {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Test
    void testGenerateResponseFromLlama3() {
        String content = chatClient.prompt()
                .system("You are a helpful assistant.")
                .user("What are the advantages of AI in hospitality?Response in Vietnamese")
                .call()
                .content();

        Assertions.assertThat(content).isNotBlank();
        System.out.println("Generated Text:\n" + content);
    }

    @Test
    void testGenerateEmbeddingVector() {
        float[] vector = embeddingModel.embed("AI helps manage hotel operations.Response in Vietnamese");
        Assertions.assertThat(vector).isNotEmpty();
        System.out.println("Vector length: " + vector.length);
    }
}
