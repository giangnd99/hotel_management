package com.poly.ai.management.domain.entity.rag;

import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.EmbeddingID;
import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.ai.management.domain.valueobject.PromptID;
import com.poly.domain.entity.AggregateRoot;


public class Embedding extends AggregateRoot<EmbeddingID> {
    private float[] vector;
    private final PromptID promptId;
    private final AiModelID modelId;

    private Embedding(Builder builder) {
        super.setId(builder.embeddingId);
        this.vector = builder.vector;
        this.promptId = builder.promptID;
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
        if (promptId == null || promptId.getValue().isEmpty()) {
            throw new AiDomainException("Data ID cannot be empty!");
        }
        if (modelId == null || modelId.getValue().isEmpty()) {
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

    public float[] getVectorArray() {
        return vector;
    }

    public float[] getVector() {
        return vector.clone(); // tránh lộ reference gốc
    }


    public PromptID getPromptId() {
        return promptId;
    }

    public AiModelID getModelId() {
        return modelId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private EmbeddingID embeddingId;
        private float[] vector;
        private PromptID promptID;
        private AiModelID modelId;

        public Builder() {
        }

        public Builder embeddingId(EmbeddingID val) {
            embeddingId = val;
            return this;
        }

        public Builder vector(float[] val) {
            vector = val;
            return this;
        }

        public Builder promptId(PromptID val) {
            promptID = val;
            return this;
        }

        public Builder modelId(AiModelID val) {
            modelId = val;
            return this;
        }

        public Embedding build() {
            return new Embedding(this);
        }
    }
}