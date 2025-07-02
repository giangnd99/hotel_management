package com.poly.ai.management.domain.handler.ai;

import com.poly.ai.management.domain.AiDomainService;
import com.poly.ai.management.domain.entity.AiModel;
import com.poly.ai.management.domain.port.output.repository.AiModelRepository;
import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.service.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
public class CreateAiModelHandler extends BaseHandler<AiDomainService, AiModelRepository> {

    public CreateAiModelHandler(AiDomainService domainService, AiModelRepository aiModelRepository) {
        super(domainService, aiModelRepository);
    }

    @Transactional
    public AiModel createAiModel(AiModel model) {
        // Gán ID cho AiModel
//        model.setId(new AiModelID(UUID.randomUUID().toString()));
        // Gọi domainService service để xác thực
        AiModel validatedModel = domainService.validateAndInitiateAiModel(model);
        // Lưu vào repository
        AiModel savedModel = repository.save(validatedModel);
        log.info("Created AiModel with id: {}", savedModel.getId().getValue());
        return savedModel;
    }
}
