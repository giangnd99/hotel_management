package com.poly.paymentdataaccess.repository.impl;

import com.poly.paymentdataaccess.entity.InvoiceServiceEntity;
import com.poly.paymentdataaccess.mapper.InvoiceServiceMapper;
import com.poly.paymentdataaccess.repository.InvoiceServiceJpaRepository;
import com.poly.paymentdomain.model.entity.InvoiceService;
import com.poly.paymentdomain.output.InvoiceServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InvoiceServiceRepositoryImpl implements InvoiceServiceRepository {

    private final InvoiceServiceJpaRepository invoiceServiceJpaRepository;

    @Override
    public List<InvoiceService> findAllByBookingId(UUID bookingId) {
        List<InvoiceServiceEntity> entities = invoiceServiceJpaRepository.findAllByInvoiceBookingId(bookingId);
        List<InvoiceService> domains = entities.stream().map(InvoiceServiceMapper::toDomain).collect(Collectors.toList());
        return domains;
    }

    @Override
    public InvoiceService save(InvoiceService object) {
        InvoiceServiceEntity entity = InvoiceServiceMapper.toEntity(object);
        entity = invoiceServiceJpaRepository.save(entity);
        return InvoiceServiceMapper.toDomain(entity);
    }

    @Override
    public InvoiceService update(InvoiceService object) {
        InvoiceServiceEntity entity = InvoiceServiceMapper.toEntity(object);
        entity = invoiceServiceJpaRepository.save(entity);
        return InvoiceServiceMapper.toDomain(entity);
    }

    @Override
    public void delete(UUID uuid) {
        InvoiceServiceEntity entity = invoiceServiceJpaRepository.findById(uuid).orElse(null);
        invoiceServiceJpaRepository.delete(entity);
    }

    @Override
    public Optional<InvoiceService> findById(UUID uuid) {
        InvoiceServiceEntity entity = invoiceServiceJpaRepository.findById(uuid).orElse(null);
        InvoiceService domain = InvoiceServiceMapper.toDomain(entity);
        return Optional.ofNullable(domain);
    }

    @Override
    public List<InvoiceService> findAll() {
        List<InvoiceServiceEntity> entities = invoiceServiceJpaRepository.findAll();
        List<InvoiceService> domains = entities.stream().map(InvoiceServiceMapper::toDomain).collect(Collectors.toList());
        return domains;
    }

    @Override
    public List<InvoiceService> findAllById(UUID uuid) {
        return List.of();
    }

    @Override
    public InvoiceService remove(InvoiceService id) {
        return null;
    }
}
