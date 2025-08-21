package com.poly.booking.management.dao.customer.entity;

import com.poly.booking.management.dao.booking.entity.BookingEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
@Entity
public class CustomerEntity {

    @Id
    @Column(columnDefinition = "uuid",updatable = false)
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
