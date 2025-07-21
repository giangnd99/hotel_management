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
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

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

    @Bean
    public VectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(768)                    // Optional: defaults to model dimensions or 1536
                .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
                .indexType(HNSW)                     // Optional: defaults to HNSW
                .initializeSchema(true)              // Optional: defaults to false
                .schemaName("public")                // Optional: defaults to "public"
                .vectorTableName("vector_store")     // Optional: defaults to "vector_store"
                .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
                .build();
    }
}
