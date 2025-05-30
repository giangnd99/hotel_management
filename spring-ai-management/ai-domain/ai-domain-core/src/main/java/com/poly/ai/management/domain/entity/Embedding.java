package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.EmbeddingID;
import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.domain.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Embedding extends BaseEntity<EmbeddingID> {
    private float[] vector;
    private final String dataId;
    private final String modelId;

    private Embedding(Builder builder) {
        super.setId(builder.embeddingId);
        this.vector = builder.vector;
        this.dataId = builder.dataId;
        this.modelId = builder.modelId;
    }

    public void setVector(float[] vector) {
        if (vector == null || vector.length == 0) {
            throw new AiDomainException("Embedding vector cannot be empty!");
        }
        this.vector = vector.clone();
    }

    public void validate() {
        if (vector == null || vector.length == 0) {
            throw new AiDomainException("Embedding vector cannot be empty!");
        }
        if (dataId == null || dataId.isEmpty()) {
            throw new AiDomainException("Data ID cannot be empty!");
        }
        if (modelId == null || modelId.isEmpty()) {
            throw new AiDomainException("Model ID cannot be empty!");
        }
    }

    public void normalize() {
        if (vector == null || vector.length == 0) {
            throw new AiDomainException("Cannot normalize empty vector!");
        }
        float sum = 0;
        for (float v : vector) {
            sum += v * v;
        }
        float norm = (float) Math.sqrt(sum);
        if (norm == 0) {
            throw new AiDomainException("Cannot normalize zero vector!");
        }
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= norm;
        }
        if (norm == 0) {
            throw new AiDomainException("Cannot normalize zero vector!");
        }
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= norm;
        }
    }

    public double calculateSimilarity(Embedding other) {
        if (vector == null || other.vector == null || vector.length != other.vector.length) {
            throw new AiDomainException("Invalid vectors for similarity calculation!");
        }
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vector.length; i++) {
            dotProduct += vector[i] * other.vector[i];
            normA += Math.pow(vector[i], 2);
            normB += Math.pow(other.vector[i], 2);
        }
        double norm = Math.sqrt(normA) * Math.sqrt(normB);
        if (norm == 0) {
            throw new AiDomainException("Cannot calculate similarity with zero norm!");
        }
        return dotProduct / norm;
    }

    public List<Float> getVector() {
        List<Float> vectorList = new ArrayList<>();
        for (float v : vector) {
            vectorList.add(v);
        }
        return vectorList;
    }

    public String getDataId() {
        return dataId;
    }

    public String getModelId() {
        return modelId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private EmbeddingID embeddingId;
        private float[] vector;
        private String dataId;
        private String modelId;

        private Builder() {
        }

        public Builder embeddingId(EmbeddingID val) {
            embeddingId = val;
            return this;
        }

        public Builder vector(float[] val) {
            vector = val;
            return this;
        }

        public Builder dataId(String val) {
            dataId = val;
            return this;
        }

        public Builder modelId(String val) {
            modelId = val;
            return this;
        }

        public Embedding build() {
            return new Embedding(this);
        }
    }
}