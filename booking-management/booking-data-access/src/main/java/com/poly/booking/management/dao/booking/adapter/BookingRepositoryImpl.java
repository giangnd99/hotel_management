package com.poly.booking.management.dao.booking.adapter;

import com.poly.booking.management.dao.booking.entity.BookingEntity;
import com.poly.booking.management.dao.booking.mapper.BookingDataAccessMapper;
import com.poly.booking.management.dao.booking.repository.BookingJpaRepository;
import com.poly.booking.management.domain.entity.Booking;

import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.domain.valueobject.BookingId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    
    @Override
    public Long countTodayBookings() {
        return bookingJpaRepository.countTodayBookings(LocalDate.now());
    }
    
    @Override
    public Long countTodayBookingsByStatus(String status) {
        return bookingJpaRepository.countTodayBookingsByStatus(LocalDate.now(), status);
    }
    
    @Override
    public Double getTodayTotalRevenue() {
        return bookingJpaRepository.getTodayTotalRevenue(LocalDate.now());
    }
    
    @Override
    public Double getTodayAverageBookingValue() {
        return bookingJpaRepository.getTodayAverageBookingValue(LocalDate.now());
    }
    
    @Override
    public List<Booking> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookingEntity> bookingPage = bookingJpaRepository.findAll(pageable);
        return bookingPage.getContent().stream()
                .map(bookingDataAccessMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(BookingId bookingId) {
        bookingJpaRepository.deleteById(bookingId.getValue());
    }
    
    @Override
    public List<Booking> searchBookings(String customerName, String customerEmail, String roomNumber,
                                       LocalDate checkInDate, LocalDate checkOutDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookingEntity> bookingPage = bookingJpaRepository.searchBookings(
                customerName, customerEmail, roomNumber, checkInDate, checkOutDate, pageable);
        return bookingPage.getContent().stream()
                .map(bookingDataAccessMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Booking> filterBookingsByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookingEntity> bookingPage = bookingJpaRepository.findByStatus(status, pageable);
        return bookingPage.getContent().stream()
                .map(bookingDataAccessMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Booking> filterBookingsByDateRange(LocalDate fromDate, LocalDate toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookingEntity> bookingPage = bookingJpaRepository.findByDateRange(fromDate, toDate, pageable);
        return bookingPage.getContent().stream()
                .map(bookingDataAccessMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Booking> findBookingsByCustomerId(UUID customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookingEntity> bookingPage = bookingJpaRepository.findByCustomerId(customerId, pageable);
        return bookingPage.getContent().stream()
                .map(bookingDataAccessMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Booking> findCustomerBookingHistory(UUID customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookingEntity> bookingPage = bookingJpaRepository.findCustomerBookingHistory(customerId, pageable);
        return bookingPage.getContent().stream()
                .map(bookingDataAccessMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
//
//    @Override
//    public List<Booking> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate,
//                                           Integer numberOfGuests, Long roomTypeId,
//                                           Integer minPrice, Integer maxPrice) {
//        List<BookingEntity> bookingEntities = bookingJpaRepository.findAvailableRooms(
//                checkInDate, checkOutDate, numberOfGuests, roomTypeId, minPrice, maxPrice);
//        return bookingEntities.stream()
//                .map(bookingDataAccessMapper::toDomainEntity)
//                .collect(Collectors.toList());
//    }
}
