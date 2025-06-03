package com.poly.ai.management.domain.handler.ai;

import com.poly.ai.management.domain.AiDomainService;
import com.poly.ai.management.domain.entity.AiModel;
import com.poly.ai.management.domain.entity.Prompt;
import com.poly.ai.management.domain.entity.Response;
import com.poly.ai.management.domain.port.output.repository.AiModelRepository;
import com.poly.ai.management.domain.port.output.repository.PromptRepository;
import com.poly.ai.management.domain.port.output.repository.ResponseRepository;
import com.poly.ai.management.domain.valueobject.PromptID;
import com.poly.ai.management.domain.valueobject.ResponseID;
import com.poly.service.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
public class ProcessPromptHandler extends BaseHandler<AiDomainService, PromptRepository> {

    private final ChatClient chatClient;
    private final AiModelRepository aiModelRepository;
    private final ResponseRepository responseRepository;

    public ProcessPromptHandler(AiDomainService domainService, PromptRepository promptRepository, ChatClient chatClient, AiModelRepository aiModelRepository, ResponseRepository responseRepository) {
        super(domainService, promptRepository);
        this.chatClient = chatClient;
        this.aiModelRepository = aiModelRepository;
        this.responseRepository = responseRepository;
    }

    @Transactional
    public Response processPrompt(Prompt prompt) {
        // Gán ID cho Prompt
//        prompt.setId(new PromptID(UUID.randomUUID().toString()));
        // Lưu Prompt trước khi xử lý
        Prompt savedPrompt = repository.save(prompt);

        // Lấy AiModel từ repository
        AiModel model = aiModelRepository.findById(savedPrompt.getModelId().getValue())
                .orElseThrow(() -> new IllegalArgumentException("AI Model not found with id: " + savedPrompt.getModelId()));

        // Gọi domainService service để xử lý Prompt
        Response response = domainService.processPrompt(savedPrompt, model);

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
}
