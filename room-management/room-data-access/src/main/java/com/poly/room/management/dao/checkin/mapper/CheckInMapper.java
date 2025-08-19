package com.poly.room.management.dao.checkin.mapper;

import com.poly.room.management.dao.checkin.entity.CheckInEntity;
import com.poly.room.management.domain.entity.CheckIn;
import com.poly.room.management.domain.valueobject.CheckInId;
import com.poly.room.management.domain.valueobject.GuestId;
import com.poly.room.management.domain.valueobject.RoomId;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CheckInMapper {

    public CheckIn toDomain(CheckInEntity entity) {
        if (entity == null) {
            return null;
        }

        return CheckIn.builder()
                .id(CheckInId.of(entity.getId()))
                .bookingId(entity.getBookingId())
                .guestId(GuestId.of(entity.getGuestId()))
                .roomId(RoomId.of(entity.getRoomId()))
                .roomNumber(entity.getRoomNumber())
                .checkInDate(entity.getCheckInDate())
                .checkOutDate(entity.getCheckOutDate())
                .checkInTime(entity.getCheckInTime())
                .checkOutTime(entity.getCheckOutTime())
                .numberOfGuests(entity.getNumberOfGuests())
                .specialRequests(entity.getSpecialRequests())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .checkedInBy(entity.getCheckedInBy())
                .checkedOutBy(entity.getCheckedOutBy())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public CheckInEntity toEntity(CheckIn domain) {
        if (domain == null) {
            return null;
        }

        CheckInEntity.CheckInStatus status = null;
        if (domain.getStatus() != null) {
            try {
                status = CheckInEntity.CheckInStatus.valueOf(domain.getStatus());
            } catch (IllegalArgumentException e) {
                // Handle unknown status values
                status = CheckInEntity.CheckInStatus.PENDING;
            }
        }

        return CheckInEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .bookingId(domain.getBookingId())
                .guestId(domain.getGuestId() != null ? domain.getGuestId().getValue() : null)
                .roomId(domain.getRoomId() != null ? domain.getRoomId().getValue() : null)
                .roomNumber(domain.getRoomNumber())
                .checkInDate(domain.getCheckInDate())
                .checkOutDate(domain.getCheckOutDate())
                .checkInTime(domain.getCheckInTime())
                .checkOutTime(domain.getCheckOutTime())
                .numberOfGuests(domain.getNumberOfGuests())
                .specialRequests(domain.getSpecialRequests())
                .status(status)
                .checkedInBy(domain.getCheckedInBy())
                .checkedOutBy(domain.getCheckedOutBy())
                .notes(domain.getNotes())
                .createdAt(domain.getCreatedAt() != null ? domain.getCreatedAt() : LocalDateTime.now())
                .updatedAt(domain.getUpdatedAt() != null ? domain.getUpdatedAt() : LocalDateTime.now())
                .build();
    }
}
