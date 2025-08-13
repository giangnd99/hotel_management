package com.poly.paymentdataaccess.repository.impl;

import com.poly.paymentdataaccess.entity.InvoiceRestaurantEntity;
import com.poly.paymentdataaccess.mapper.InvoiceRestaurantMapper;
import com.poly.paymentdataaccess.repository.InvoiceRestaurantJpaRepository;
import com.poly.paymentdomain.model.entity.InvoiceRestaurant;
import com.poly.paymentdomain.output.InvoiceRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InvoiceRestaurantRepositoryImpl implements InvoiceRestaurantRepository {

    private final InvoiceRestaurantJpaRepository invoiceRestaurantJpaRepository;

    @Override
    public List<InvoiceRestaurant> findAllByBookingId(UUID bookingId) {
        List<InvoiceRestaurantEntity> entities = invoiceRestaurantJpaRepository.findAllByInvoiceBookingId(bookingId);
        List<InvoiceRestaurant> invoiceRestaurants = entities.stream().map(InvoiceRestaurantMapper::toDomain).toList();
        return invoiceRestaurants;
    }

    @Override
    public InvoiceRestaurant save(InvoiceRestaurant object) {
        InvoiceRestaurantEntity entity = InvoiceRestaurantMapper.toEntity(object);
        entity = invoiceRestaurantJpaRepository.save(entity);
        return InvoiceRestaurantMapper.toDomain(entity);
    }

    @Override
    public InvoiceRestaurant update(InvoiceRestaurant object) {
        InvoiceRestaurantEntity entity = InvoiceRestaurantMapper.toEntity(object);
        entity = invoiceRestaurantJpaRepository.save(entity);
        return InvoiceRestaurantMapper.toDomain(entity);
    }

    @Override
    public void delete(UUID uuid) {
        InvoiceRestaurantEntity entity = invoiceRestaurantJpaRepository.findById(uuid).orElse(null);
        invoiceRestaurantJpaRepository.delete(entity);
    }

    @Override
    public Optional<InvoiceRestaurant> findById(UUID uuid) {
        InvoiceRestaurantEntity entity = invoiceRestaurantJpaRepository.findById(uuid).orElse(null);
        return Optional.ofNullable(InvoiceRestaurantMapper.toDomain(entity));
    }

    @Override
    public List<InvoiceRestaurant> findAll() {
        List<InvoiceRestaurantEntity> entities = invoiceRestaurantJpaRepository.findAll();
        List<InvoiceRestaurant> domains = entities.stream().map(InvoiceRestaurantMapper::toDomain).toList();
        return domains;
    }

    @Override
    public List<InvoiceRestaurant> findAllById(UUID uuid) {
        return List.of();
    }

    @Override
    public InvoiceRestaurant remove(InvoiceRestaurant id) {
        return null;
    }
}
