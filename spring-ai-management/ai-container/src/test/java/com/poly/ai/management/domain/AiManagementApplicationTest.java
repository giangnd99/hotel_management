package com.poly.ai.management.domain;

import com.poly.ai.management.domain.entity.*;
import com.poly.ai.management.domain.port.input.service.AiHotelApplicationService;
import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.DatasetID;
import com.poly.ai.management.domain.valueobject.PromptID;
import com.poly.ai.management.domain.valueobject.TrainingJobID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiManagementApplicationTest {

    @Autowired
    private AiHotelApplicationService aiService;

    @Autowired
    private EmbeddingModel embeddingModel; // test embedding nếu cần thực tế

    @Test
    public void fullAiWorkflowTest() {
        // --- 1. Tạo mô hình AI ---
        AiModel model = AiModel.Builder.builder()
                .aiModelID(new AiModelID("test-model"))
                .name("llama3")
                .version("1.0")
                .provider("Local LLM Model")
                .build();

        AiModel createdModel = aiService.createAiModel(model);
        Assertions.assertThat(createdModel).isNotNull();
        Assertions.assertThat(createdModel.getId()).isEqualTo(new AiModelID("test-model"));
        Assertions.assertThat(createdModel.getName()).isEqualTo("llama3");
        Assertions.assertThat(createdModel.getProvider()).isEqualTo("Local LLM Model");

        // --- 2. Tạo Prompt và xử lý ---
        Prompt prompt = Prompt.Builder.builder()
                .id(new PromptID("test-prompt"))
                .modelId(createdModel.getId())
                .text("What are the benefits of AI in hotels?")
                .build();

        Response response = aiService.processPrompt(prompt);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getGeneratedText()).isNotEmpty();

        // --- 3. Sinh Embedding từ Prompt ---
        Embedding embedding = aiService.generateEmbedding(prompt);
        Assertions.assertThat(embedding).isNotNull();
        Assertions.assertThat(embedding.getVectorArray()).isNotEmpty();
        // Giả sử kích thước vector là 768 (dựa trên mô hình nomic-embed-text)
        Assertions.assertThat(embedding.getVectorArray()).hasSize(768);

        // --- 4. Chuẩn bị Dataset ---
        Dataset dataset = Dataset.Builder.builder()
                .id(new DatasetID("test-dataset"))
                .name("Hotel Training Set")
                .source("internal")
                .size(200)
                .build();

        Dataset preparedDataset = aiService.prepareDataset(dataset);
        Assertions.assertThat(preparedDataset).isNotNull();
        Assertions.assertThat(preparedDataset.getId()).isEqualTo(new DatasetID("test-dataset"));
        Assertions.assertThat(preparedDataset.getName()).isEqualTo("Hotel Training Set");
        Assertions.assertThat(preparedDataset.getSource()).isEqualTo("internal");
        Assertions.assertThat(preparedDataset.getSize()).isEqualTo(200);

        // --- 5. Bắt đầu Training ---
        TrainingJob job = TrainingJob.builder()
                .trainingJobId(new TrainingJobID("test-job"))
                .modelId(createdModel.getId())
                .datasetId(preparedDataset.getId())
                .build();

        TrainingJob startedJob = aiService.startTraining(job);
        Assertions.assertThat(startedJob).isNotNull();
        Assertions.assertThat(startedJob.getId()).isEqualTo(new TrainingJobID("test-job"));
        Assertions.assertThat(startedJob.getStatus()).isEqualTo("RUNNING");

        // --- 6. Hoàn thành Training ---
        TrainingJob completedJob = aiService.completeTraining(startedJob);
        Assertions.assertThat(completedJob).isNotNull();
        Assertions.assertThat(completedJob.getStatus()).isEqualTo("COMPLETED");

        // --- 7. Thử fail một job ---
        TrainingJob failJob = TrainingJob.builder()
                .trainingJobId(new TrainingJobID("test-fail-job"))
                .modelId(createdModel.getId())
                .datasetId(preparedDataset.getId())
                .build();

        TrainingJob startedFailJob = aiService.startTraining(failJob);
        TrainingJob failed = aiService.failTraining(startedFailJob, "Test error");
        Assertions.assertThat(failed).isNotNull();
        Assertions.assertThat(failed.getStatus()).isEqualTo("FAILED");
        Assertions.assertThat(failed.getErrorMessages()).contains("Test error");
    }
}