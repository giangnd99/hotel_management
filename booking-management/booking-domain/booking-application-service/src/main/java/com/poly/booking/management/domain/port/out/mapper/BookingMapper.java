package com.poly.booking.management.domain.port.out.mapper;

import com.poly.booking.management.domain.dto.BookingDto;
import com.poly.booking.management.domain.entity.Booking;

public interface BookingMapper {
    
    /**
     * Chuyển đổi từ domain entity sang DTO
     * 
     * @param booking Domain entity
     * @return BookingDto
     */
    BookingDto toDto(Booking booking);
    
    /**
     * Chuyển đổi từ DTO sang domain entity
     * 
     * @param dto BookingDto
     * @return Domain entity
     */
    Booking toDomainEntity(BookingDto dto);
}
