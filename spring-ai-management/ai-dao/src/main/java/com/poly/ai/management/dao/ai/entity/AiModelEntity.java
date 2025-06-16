package com.poly.ai.management.dao.ai.entity;

import com.poly.ai.management.domain.valueobject.AiModelID;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ai_models")
@Getter
@Setter
public class AiModelEntity {

    @Id
    private String id;

    private String name;

    private String provider;

    private String version;

    private boolean isActive;
}
