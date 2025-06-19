package com.poly.ai.management.domain.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiClientConfiguration {

    @Value("${spring.ai.ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${spring.ai.ollama.chat.options.model:llama3}")
    private String ollamaChatModel;

    @Value("${spring.ai.ollama.embedding.options.model:nomic-embed-text}")
    private String ollamaEmbeddingModel;

    @Bean
    public OllamaApi ollamaApi() {
        return OllamaApi.builder()
                .baseUrl(ollamaBaseUrl)
                .build();
    }

    @Bean
    public OllamaChatModel ollamaChatModel(OllamaApi ollamaApi) {
        ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();
        ObservationRegistry observationRegistry = ObservationRegistry.NOOP;
        ModelManagementOptions modelManagementOptions = ModelManagementOptions.defaults();

        return new OllamaChatModel(
                ollamaApi,
                OllamaOptions.builder().model(ollamaChatModel).build(),
                toolCallingManager,
                observationRegistry,
                modelManagementOptions
        );
    }

    @Bean
    public EmbeddingModel embeddingModel(OllamaApi ollamaApi) {
        ObservationRegistry observationRegistry = ObservationRegistry.NOOP;
        ModelManagementOptions modelManagementOptions = ModelManagementOptions.defaults();

        return new OllamaEmbeddingModel(
                ollamaApi,
                OllamaOptions.builder().model(ollamaEmbeddingModel).build(),
                observationRegistry,
                modelManagementOptions
        );
    }

    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel).build();
    }
}
