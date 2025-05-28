package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.EmbeddingID;
import com.poly.domain.entity.BaseEntity;

import java.util.List;

public class Embedding extends BaseEntity<EmbeddingID> {

    private List<Double> vector;
    private String dataId;

    public List<Double> getVector() {
        return vector;
    }

    public String getDataId() {
        return dataId;
    }

    private Embedding(Builder builder) {
        super.setId(builder.embeddingID);
        vector = builder.vector;
        dataId = builder.dataId;
    }


    public static final class Builder {
        private EmbeddingID embeddingID;
        private List<Double> vector;
        private String dataId;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder EmbeddingID(EmbeddingID val) {
            embeddingID = val;
            return this;
        }

        public Builder vector(List<Double> val) {
            vector = val;
            return this;
        }

        public Builder dataId(String val) {
            dataId = val;
            return this;
        }

        public Embedding build() {
            return new Embedding(this);
        }
    }
}
