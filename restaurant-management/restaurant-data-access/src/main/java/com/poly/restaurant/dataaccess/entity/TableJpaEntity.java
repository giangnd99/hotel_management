package com.poly.restaurant.dataaccess.entity;

import com.poly.restaurant.domain.entity.TableStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tables")
public class TableJpaEntity {
    @Id
    @Column(name = "table_id")
    private String id;

    @Column(name = "number")
    private Integer number;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TableStatus status;
}