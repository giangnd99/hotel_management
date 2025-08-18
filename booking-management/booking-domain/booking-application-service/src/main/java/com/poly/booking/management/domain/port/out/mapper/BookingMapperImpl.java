package com.poly.booking.management.domain.port.out.mapper;

import com.poly.booking.management.domain.dto.BookingDto;
import com.poly.booking.management.domain.entity.Booking;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class BookingMapperImpl implements BookingMapper {
    
    @Override
    public BookingDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        
        BookingDto dto = new BookingDto();
        dto.setBookingId(booking.getId().getValue());
        
        if (booking.getCustomer() != null) {
            dto.setCustomerId(booking.getCustomer().getId().getValue());
            dto.setCustomerName(booking.getCustomer().getName());
            dto.setCustomerEmail(booking.getCustomer().getEmail());
        }
        
        if (booking.getCheckInDate() != null) {
            dto.setCheckInDate(LocalDate.from(booking.getCheckInDate().getValue()));
        }
        
        if (booking.getCheckOutDate() != null) {
            dto.setCheckOutDate(LocalDate.from(booking.getCheckOutDate().getValue()));
        }
        
        if (booking.getTotalPrice() != null) {
            dto.setTotalAmount(booking.getTotalPrice().getAmount());
        }
        
        if (booking.getStatus() != null) {
            dto.setStatus(booking.getStatus().name());
        }
        
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        
        // Set room information if available
        if (booking.getBookingRooms() != null && !booking.getBookingRooms().isEmpty()) {
            var firstRoom = booking.getBookingRooms().get(0);
            if (firstRoom.getRoom() != null) {
                dto.setRoomId(firstRoom.getRoom().getId().getValue());
                dto.setRoomNumber(firstRoom.getRoom().getRoomNumber());
            }
        }
        
        return dto;
    }
    
    @Override
    public Booking toDomainEntity(BookingDto dto) {
        if (dto == null) {
            return null;
        }
        
        // This method would need more context to properly create a domain entity
        // For now, returning null as it's not typically used in this direction
        return null;
    }
}
