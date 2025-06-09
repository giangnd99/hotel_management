package com.poly.ai.management.dao.ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "documents", indexes = {
        @Index(name = "idx_source", columnList = "source"),
        @Index(name = "idx_content_tsv", columnList = "content_tsv", columnList = "content_tsv", method = "GIN")
})
@Data
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 50)
    private String source;

    @Column(name = "content_tsv", columnDefinition = "tsvector")
    @Type(type = "org.hibernate.type.TextType")
    private String contentTsv;

    @PrePersist
    @PreUpdate
    public void updateTsvector() {
        this.contentTsv = "to_tsvector('english', content)";
    }
}
