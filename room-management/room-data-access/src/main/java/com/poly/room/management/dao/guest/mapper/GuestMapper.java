package com.poly.room.management.dao.guest.mapper;

import com.poly.room.management.dao.guest.entity.GuestEntity;
import com.poly.room.management.domain.entity.Guest;
import com.poly.room.management.domain.valueobject.GuestId;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class GuestMapper {

    public Guest toDomain(GuestEntity entity) {
        if (entity == null) {
            return null;
        }

        return Guest.builder()
                .id(GuestId.of(entity.getId()))
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .idNumber(entity.getIdNumber())
                .idType(entity.getIdType())
                .nationality(entity.getNationality())
                .address(entity.getAddress())
                .dateOfBirth(entity.getDateOfBirth())
                .gender(entity.getGender())
                .specialRequests(entity.getSpecialRequests())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public GuestEntity toEntity(Guest domain) {
        if (domain == null) {
            return null;
        }

        GuestEntity.GuestStatus status = null;
        if (domain.getStatus() != null) {
            try {
                status = GuestEntity.GuestStatus.valueOf(domain.getStatus());
            } catch (IllegalArgumentException e) {
                // Handle unknown status values
                status = GuestEntity.GuestStatus.ACTIVE;
            }
        }

        return GuestEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .firstName(domain.getFirstName())
                .lastName(domain.getLastName())
                .fullName(domain.getFullName())
                .phone(domain.getPhone())
                .email(domain.getEmail())
                .idNumber(domain.getIdNumber())
                .idType(domain.getIdType())
                .nationality(domain.getNationality())
                .address(domain.getAddress())
                .dateOfBirth(domain.getDateOfBirth())
                .gender(domain.getGender())
                .specialRequests(domain.getSpecialRequests())
                .status(status)
                .createdAt(domain.getCreatedAt() != null ? domain.getCreatedAt() : LocalDateTime.now())
                .updatedAt(domain.getUpdatedAt() != null ? domain.getUpdatedAt() : LocalDateTime.now())
                .build();
    }
}
