package com.poly.ai.management.dao.ai.entity;

import com.poly.ai.management.domain.valueobject.DatasetID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "datasets")
@Getter
@Setter
public class DatasetEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "source")
    private String source;

    private long size;

    @Embedded
    private DatasetID datasetId;

    public void setId(DatasetID id) {
        this.id = id.getValue();
        this.datasetId = id;
    }
}
