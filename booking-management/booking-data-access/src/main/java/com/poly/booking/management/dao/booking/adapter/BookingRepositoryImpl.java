package com.poly.booking.management.dao.booking.adapter;

import com.poly.booking.management.dao.booking.entity.BookingEntity;
import com.poly.booking.management.dao.booking.mapper.BookingDataAccessMapper;
import com.poly.booking.management.dao.booking.repository.BookingJpaRepository;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.domain.valueobject.BookingId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingDataAccessMapper bookingDataAccessMapper;
    private final BookingJpaRepository bookingJpaRepository;

    @Override
    public Booking save(Booking booking) {
        BookingEntity entity = bookingJpaRepository.save(bookingDataAccessMapper.toEntity(booking));
        return bookingDataAccessMapper.toDomainEntity(entity);
    }

    @Override
    public Optional<Booking> findById(BookingId bookingId) {
        return bookingJpaRepository.findById(bookingId.getValue())
                .map(bookingDataAccessMapper::toDomainEntity);
    }
}
