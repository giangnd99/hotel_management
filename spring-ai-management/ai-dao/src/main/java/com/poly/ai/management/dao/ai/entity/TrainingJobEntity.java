package com.poly.ai.management.dao.ai.entity;

import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.DatasetID;
import com.poly.ai.management.domain.valueobject.TrainingJobID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "training_jobs")
@Getter
@Setter
public class TrainingJobEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "status")
    private String status;

    @Embedded
    private TrainingJobID trainingJobId;

    @Embedded
    private AiModelID modelId;

    @Embedded
    private DatasetID datasetId;

    public void setId(TrainingJobID id) {
        this.id = id.getValue();
        this.trainingJobId = id;
    }
}
