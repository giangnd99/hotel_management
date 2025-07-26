package com.poly.ai.management.dao.ai.entity;

import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.PromptID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prompts")
@Getter
@Setter
public class PromptEntity {

    @Id
    private String id;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    private String aiModelID;

}
