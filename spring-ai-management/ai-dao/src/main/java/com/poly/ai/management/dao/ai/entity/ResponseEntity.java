package com.poly.ai.management.dao.ai.entity;

import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.PromptID;
import com.poly.ai.management.domain.valueobject.ResponseID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "responses")
@Getter
@Setter
public class ResponseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "generated_text")
    private String generatedText;

    @Embedded
    private ResponseID responseId;

    @Embedded
    private AiModelID aiModelId;

    @Embedded
    private PromptID promptId;

    public void setId(ResponseID id) {
        this.id = id.getValue();
        this.responseId = id;
    }
}
