package com.poly.paymentdataaccess.repository.impl;

import com.poly.paymentdataaccess.entity.InvoiceVoucherEntity;
import com.poly.paymentdataaccess.mapper.InvoiceVoucherMapper;
import com.poly.paymentdataaccess.repository.InvoiceVoucherJpaRepository;
import com.poly.paymentdomain.model.entity.InvoiceVoucher;
import com.poly.paymentdomain.output.InvoiceVoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InvoiceVoucherRepositoryImpl implements InvoiceVoucherRepository {

    private final InvoiceVoucherJpaRepository invoiceVoucherJpaRepository;

    @Override
    public InvoiceVoucher save(InvoiceVoucher object) {
        InvoiceVoucherEntity entity = InvoiceVoucherMapper.toEntity(object);
        entity = invoiceVoucherJpaRepository.save(entity);
        return InvoiceVoucherMapper.toDomain(entity);
    }

    @Override
    public InvoiceVoucher update(InvoiceVoucher object) {
        InvoiceVoucherEntity entity = InvoiceVoucherMapper.toEntity(object);
        entity = invoiceVoucherJpaRepository.save(entity);
        return InvoiceVoucherMapper.toDomain(entity);
    }

    @Override
    public void delete(UUID uuid) {
        InvoiceVoucherEntity entity = invoiceVoucherJpaRepository.findById(uuid).orElse(null);
        invoiceVoucherJpaRepository.delete(entity);
    }

    @Override
    public Optional<InvoiceVoucher> findById(UUID uuid) {
        InvoiceVoucherEntity entity = invoiceVoucherJpaRepository.findById(uuid).orElse(null);
        InvoiceVoucher domain = InvoiceVoucherMapper.toDomain(entity);
        return Optional.ofNullable(domain);
    }

    @Override
    public List<InvoiceVoucher> findAll() {
        List<InvoiceVoucherEntity> entity = invoiceVoucherJpaRepository.findAll();
        List<InvoiceVoucher> domain = entity.stream().map(InvoiceVoucherMapper::toDomain).collect(Collectors.toList());
        return domain;
    }

    @Override
    public List<InvoiceVoucher> findAllById(UUID uuid) {
        return List.of();
    }

    @Override
    public InvoiceVoucher remove(InvoiceVoucher id) {
        return null;
    }
}
