package com.poly.ai.management.domain;


import com.poly.ai.management.domain.entity.*;
import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.ai.management.domain.port.input.service.AiHotelApplicationService;
import com.poly.ai.management.domain.port.output.repository.*;
import com.poly.ai.management.domain.valueobject.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AiHotelApplicationServiceImpl implements AiHotelApplicationService {

    private final AiDomainService aiDomainService;
    private final AiModelRepository aiModelRepository;
    private final PromptRepository promptRepository;
    private final ResponseRepository responseRepository;
    private final EmbeddingRepository embeddingRepository;
    private final DatasetRepository datasetRepository;
    private final TrainingJobRepository trainingJobRepository;
    private final ChatClient chatClient; // Spring AI để gọi mô hình AI
    private final EmbeddingModel embeddingModel;// Spring AI để tạo embedding

    public AiHotelApplicationServiceImpl(
            AiDomainService aiDomainService,
            AiModelRepository aiModelRepository,
            PromptRepository promptRepository,
            ResponseRepository responseRepository,
            EmbeddingRepository embeddingRepository,
            DatasetRepository datasetRepository,
            TrainingJobRepository trainingJobRepository,
            ChatClient chatClient,
            EmbeddingModel embeddingModel) {
        this.aiDomainService = aiDomainService;
        this.aiModelRepository = aiModelRepository;
        this.promptRepository = promptRepository;
        this.responseRepository = responseRepository;
        this.embeddingRepository = embeddingRepository;
        this.datasetRepository = datasetRepository;
        this.trainingJobRepository = trainingJobRepository;
        this.chatClient = chatClient;
        this.embeddingModel = embeddingModel;
    }

    @Override
    @Transactional
    public AiModel createAiModel(AiModel model) {
        // Gán ID cho AiModel
        model.setId(new AiModelID(UUID.randomUUID().toString()));
        // Gọi domainService service để xác thực
        AiModel validatedModel = aiDomainService.validateAndInitiateAiModel(model);
        // Lưu vào repository
        AiModel savedModel = aiModelRepository.save(validatedModel);
        log.info("Created AiModel with id: {}", savedModel.getId().getValue());
        return savedModel;
    }

    @Override
    @Transactional
    public Response processPrompt(Prompt prompt) {
        // Gán ID cho Prompt
        prompt.setId(new PromptID(UUID.randomUUID().toString()));
        // Lưu Prompt trước khi xử lý
        Prompt savedPrompt = promptRepository.save(prompt);

        // Lấy AiModel từ repository
        AiModel model = aiModelRepository.findById(savedPrompt.getModelId().toString())
                .orElseThrow(() -> new IllegalArgumentException("AI Model not found with id: " + savedPrompt.getModelId()));

        // Gọi domainService service để xử lý Prompt
        Response response = aiDomainService.processPrompt(savedPrompt, model);

        // Gán ID và nội dung cho Response
        response.setId(new ResponseID(UUID.randomUUID().toString()));
        // Gọi Spring AI để tạo văn bản thực sự
        String generatedText = chatClient.prompt()
                .system("You are an AI assistant.")
                .user(savedPrompt.getText())
                .call()
                .content();
        response.setGeneratedText(generatedText);

        // Lưu Response
        Response savedResponse = responseRepository.save(response);
        log.info("Processed Prompt with id: {}, Response id: {}", savedPrompt.getId().getValue(), savedResponse.getId().getValue());
        return savedResponse;
    }

    @Override
    @Transactional
    public Embedding generateEmbedding(Prompt prompt) {
        // Gán ID cho Prompt nếu chưa có
        if (prompt.getId() == null) {
            prompt.setId(new PromptID(UUID.randomUUID().toString()));
            prompt = promptRepository.save(prompt);
        }

        // Lấy AiModel từ repository
        Prompt finalPrompt = prompt;
        AiModel model = aiModelRepository.findById(prompt.getModelId().getValue())
                .orElseThrow(() -> new IllegalArgumentException("AI Model not found with id: " + finalPrompt.getModelId()));

        // Gọi domainService service để tạo Embedding
        Embedding embedding = aiDomainService.generateEmbedding(prompt, model);

        // Gán ID và vector cho Embedding
        embedding.setId(new EmbeddingID(UUID.randomUUID().toString()));
        // Gọi Spring AI để tạo vector thực sự
        float[] vector = embeddingModel.embed(prompt.getText());
        embedding.setVector(vector);

        // Lưu Embedding
        Embedding savedEmbedding = embeddingRepository.save(embedding);
        log.info("Generated Embedding for Prompt id: {}, Embedding id: {}", prompt.getId().getValue(), savedEmbedding.getId().getValue());
        return savedEmbedding;
    }

    @Override
    @Transactional
    public List<Embedding> findSimilarEmbeddings(Embedding queryEmbedding, int topK) {
        if (queryEmbedding.getId() == null) {
            queryEmbedding.setId(new EmbeddingID(UUID.randomUUID().toString()));
            queryEmbedding = embeddingRepository.save(queryEmbedding);
        }

        List<Embedding> allEmbeddings = embeddingRepository.findAll();
        float[] queryVector = queryEmbedding.getVectorArray(); // dùng float[] thay vì List<Float>

        List<Embedding> embeddingsFromDb = allEmbeddings.stream()
                .filter(e -> cosineSimilarity(e.getVectorArray(), queryVector) > 0.7)
                .sorted((e1, e2) -> Double.compare(
                        cosineSimilarity(e2.getVectorArray(), queryVector),
                        cosineSimilarity(e1.getVectorArray(), queryVector)))
                .limit(topK)
                .collect(Collectors.toList());

        log.info("Found {} similar embeddings for Embedding id: {}", embeddingsFromDb.size(), queryEmbedding.getId().getValue());
        return embeddingsFromDb;
    }

    private double cosineSimilarity(float[] vectorA, float[] vectorB) {
        if (vectorA == null || vectorB == null || vectorA.length != vectorB.length) {
            throw new AiDomainException("Vectors must be non-null and of the same length for cosine similarity!");
        }

        float dotProduct = 0.0f;
        float normA = 0.0f;
        float normB = 0.0f;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += vectorA[i] * vectorA[i];
            normB += vectorB[i] * vectorB[i];
        }

        double denominator = Math.sqrt(normA) * Math.sqrt(normB);
        if (denominator == 0.0) {
            return 0.0;
        }

        return dotProduct / denominator;
    }

    @Override
    @Transactional
    public Dataset prepareDataset(Dataset dataset) {
        // Gán ID cho Dataset
        dataset.setId(new DatasetID(UUID.randomUUID().toString()));
        // Gọi domainService service để chuẩn bị Dataset
        Dataset preparedDataset = aiDomainService.prepareDataset(dataset);
        // Lưu Dataset
        Dataset savedDataset = datasetRepository.save(preparedDataset);
        log.info("Prepared Dataset with id: {}", savedDataset.getId().getValue());
        return savedDataset;
    }

    @Override
    @Transactional
    public TrainingJob startTraining(TrainingJob job) {
        // Gán ID cho TrainingJob
        job.setId(new TrainingJobID(UUID.randomUUID().toString()));
        // Lấy AiModel và Dataset từ repository
        AiModel model = aiModelRepository.findById(job.getModelId().toString())
                .orElseThrow(() -> new IllegalArgumentException("AI Model not found with id: " + job.getModelId()));
        Dataset dataset = datasetRepository.findById(job.getDatasetId().getValue())
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found with id: " + job.getDatasetId()));

        // Gọi domainService service để bắt đầu huấn luyện
        TrainingJob startedJob = aiDomainService.startTraining(job, model, dataset);
        // Lưu TrainingJob
        TrainingJob savedJob = trainingJobRepository.save(startedJob);
        log.info("Started TrainingJob with id: {}", savedJob.getId().getValue());
        return savedJob;
    }

    @Override
    @Transactional
    public TrainingJob completeTraining(TrainingJob job) {
        // Gọi domainService service để hoàn thành
        TrainingJob completedJob = aiDomainService.completeTraining(job);
        // Lưu TrainingJob
        TrainingJob savedJob = trainingJobRepository.save(completedJob);
        log.info("Completed TrainingJob with id: {}", savedJob.getId().getValue());
        return savedJob;
    }

    @Override
    @Transactional
    public TrainingJob failTraining(TrainingJob job, String errorMessage) {
        // Gọi domainService service để đánh dấu thất bại
        TrainingJob failedJob = aiDomainService.failTraining(job, errorMessage);
        // Lưu TrainingJob
        TrainingJob savedJob = trainingJobRepository.save(failedJob);
        log.info("Failed TrainingJob with id: {}, error: {}", savedJob.getId().getValue(), errorMessage);
        return savedJob;
    }
}
