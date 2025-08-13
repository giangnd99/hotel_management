package com.poly.paymentdataaccess.repository.impl;

import com.poly.paymentdataaccess.entity.InvoiceBookingEntity;
import com.poly.paymentdataaccess.mapper.InvoiceBookingMapper;
import com.poly.paymentdataaccess.repository.InvoiceBookingJpaRepository;
import com.poly.paymentdomain.model.entity.InvoiceBooking;
import com.poly.paymentdomain.output.InvoiceBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InvoiceBookingRepositoryImpl implements InvoiceBookingRepository {

    private final InvoiceBookingJpaRepository invoiceBookingJpaRepository;

    @Override
    public Optional<InvoiceBooking> findByInvoiceId(UUID invoiceId) {
        InvoiceBookingEntity invoiceBookingEntity = invoiceBookingJpaRepository.findByInvoiceId(invoiceId).orElse(null);
        return Optional.ofNullable(InvoiceBookingMapper.toDomain(invoiceBookingEntity));
    }

    @Override
    public Optional<InvoiceBooking> findByBookingId(UUID bookingId) {
        InvoiceBookingEntity invoiceBookingEntity = invoiceBookingJpaRepository.findByBookingId(bookingId).orElse(null);
        return Optional.ofNullable(InvoiceBookingMapper.toDomain(invoiceBookingEntity));
    }

    @Override
    public InvoiceBooking save(InvoiceBooking object) {
        InvoiceBookingEntity entity = InvoiceBookingMapper.toEntity(object);
        entity = invoiceBookingJpaRepository.save(entity);
        return InvoiceBookingMapper.toDomain(entity);
    }

    @Override
    public InvoiceBooking update(InvoiceBooking object) {
        InvoiceBookingEntity entity = InvoiceBookingMapper.toEntity(object);
        entity = invoiceBookingJpaRepository.save(entity);
        return InvoiceBookingMapper.toDomain(entity);
    }

    @Override
    public void delete(UUID uuid) {
        InvoiceBookingEntity invoiceBookingEntity = invoiceBookingJpaRepository.findById(uuid).orElse(null);
        invoiceBookingJpaRepository.delete(invoiceBookingEntity);
    }

    @Override
    public Optional<InvoiceBooking> findById(UUID uuid) {
        InvoiceBookingEntity invoiceBookingEntity = invoiceBookingJpaRepository.findById(uuid).orElse(null);
        return Optional.ofNullable(InvoiceBookingMapper.toDomain(invoiceBookingEntity));
    }

    @Override
    public List<InvoiceBooking> findAll() {
        List<InvoiceBookingEntity> entities = invoiceBookingJpaRepository.findAll();
        List<InvoiceBooking> domainList = entities.stream().map(InvoiceBookingMapper::toDomain).collect(Collectors.toList());
        return domainList;
    }

    @Override
    public List<InvoiceBooking> findAllById(UUID uuid) {
        List<InvoiceBookingEntity> entities = invoiceBookingJpaRepository.findAllById(Collections.singleton(uuid));
        return List.of();
    }

    @Override
    public InvoiceBooking remove(InvoiceBooking id) {
        return null;
    }
}
