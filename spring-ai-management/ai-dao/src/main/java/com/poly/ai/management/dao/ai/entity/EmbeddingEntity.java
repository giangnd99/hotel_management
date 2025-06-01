package com.poly.ai.management.dao.ai.entity;

import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.DatasetID;
import com.poly.ai.management.domain.valueobject.EmbeddingID;
import com.poly.ai.management.domain.valueobject.PromptID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Entity
@Table(name = "embeddings")
@Getter
@Setter
public class EmbeddingEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "vector", length = 10000)
    private String vectorString; // Lưu float[] dưới dạng chuỗi

    @Transient
    private float[] vector; // Không lưu trực tiếp vào DB

    @Embedded
    private EmbeddingID embeddingId;

    @Embedded
    private PromptID promptId;

    @Embedded
    private AiModelID modelId;

    public void setId(EmbeddingID id) {
        this.id = id.getValue();
        this.embeddingId = id;
    }

    public void setVector(float[] vector) {
        this.vector = vector;
        this.vectorString = Arrays.toString(vector); // Chuyển float[] thành chuỗi
    }

    @PostLoad
    public void loadVector() {
        if (vectorString != null && !vectorString.isEmpty()) {
            String[] parts = vectorString.replace("[", "").replace("]", "").split(",\\s*");
            vector = new float[parts.length];
            for (int i = 0; i < parts.length; i++) {
                try {
                    vector[i] = Float.parseFloat(parts[i]);
                } catch (NumberFormatException e) {
                    throw new AiDomainException("Invalid float format in vector string: " + parts[i]);
                }
            }
        }
    }

    // --- Chuẩn hoá vector thành vector đơn vị ---
    public void normalize() {
        if (vector == null || vector.length == 0) {
            throw new AiDomainException("Cannot normalize an empty vector!");
        }

        float norm = 0.0f;
        for (float v : vector) {
            norm += v * v;
        }

        norm = (float) Math.sqrt(norm);
        if (norm == 0.0f) {
            throw new AiDomainException("Cannot normalize a zero vector!");
        }

        for (int i = 0; i < vector.length; i++) {
            vector[i] /= norm;
        }

        setVector(vector); // Cập nhật lại vectorString sau khi chuẩn hoá
    }

    // --- Helper getter an toàn ---
    public float[] getVector() {
        if (vector == null) {
            loadVector(); // đảm bảo luôn có vector
        }
        return vector;
    }
}
