package com.poly.ai.management.dao.ai.entity;

import com.poly.ai.management.domain.valueobject.PromptID;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prompts")
@Getter
@Setter
public class PromptEntity {

    @Id
    private String id;

    private String text;

    @Embedded
    private PromptID promptID;

    public void setId(PromptID id) {
        this.id = id.getValue();
        this.promptID = id;
    }
}
