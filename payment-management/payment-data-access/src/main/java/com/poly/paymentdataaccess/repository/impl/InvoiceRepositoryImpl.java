package com.poly.paymentdataaccess.repository.impl;

import com.poly.paymentdataaccess.entity.InvoiceEntity;
import com.poly.paymentdataaccess.entity.InvoiceItemEntity;
import com.poly.paymentdataaccess.mapper.InvoiceItemMapper;
import com.poly.paymentdataaccess.mapper.InvoiceMapper;
import com.poly.paymentdataaccess.repository.InvoiceItemJpaRepository;
import com.poly.paymentdataaccess.repository.InvoiceJpaRepository;
import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.InvoiceItem;
import com.poly.paymentdomain.output.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final InvoiceJpaRepository invoiceJpaRepository;

    private final InvoiceItemJpaRepository invoiceItemJpaRepository;

    @Override
    public Invoice createInvoice(Invoice invoice) {
        var invoiceEntity = InvoiceMapper.toEntity(invoice);
        var savedInvoiceEntity = invoiceJpaRepository.save(invoiceEntity);

        var itemEntities = invoice.getItems().stream()
                .map(item -> InvoiceItemMapper.toEntity(item, savedInvoiceEntity.getId()))
                .collect(Collectors.toList());

        invoiceItemJpaRepository.saveAll(itemEntities);

        return InvoiceMapper.toDomain(savedInvoiceEntity, itemEntities);
    }


    @Override
    public Invoice updateInvoice(Invoice invoice, List<InvoiceItem> items) {
        var invoiceEntity = InvoiceMapper.toEntity(invoice);
        var savedInvoiceEntity = invoiceJpaRepository.save(invoiceEntity);

        var itemEntities = items.stream()
                .map(item -> InvoiceItemMapper.toEntity(item, savedInvoiceEntity.getId()))
                .collect(Collectors.toList());

        invoiceItemJpaRepository.saveAll(itemEntities);

        return InvoiceMapper.toDomain(savedInvoiceEntity, itemEntities);
    }

    @Override
    public void deleteInvoice(Invoice invoice) {
        invoiceJpaRepository.delete(InvoiceMapper.toEntity(invoice));
    }

    @Override
    public Optional<Invoice> findInvoiceById(UUID invoiceId) {
        Optional<InvoiceEntity> invoiceEntity = invoiceJpaRepository.findById(invoiceId);
        List<InvoiceItemEntity> itemEntities = invoiceItemJpaRepository.findByInvoiceId(invoiceId);
        return Optional.of(InvoiceMapper.toDomain(invoiceEntity.get(), itemEntities));
    }

    @Override
    public Optional<Invoice> findByBookingId(UUID bookingId) {
        Optional<InvoiceEntity> invoiceEntityOpt = invoiceJpaRepository.findByBookingId(bookingId);
        if (invoiceEntityOpt.isEmpty()) {
            return Optional.empty();
        }

        InvoiceEntity invoiceEntity = invoiceEntityOpt.get();
        List<InvoiceItemEntity> itemEntities = invoiceItemJpaRepository.findByInvoiceId(invoiceEntity.getId());
        return Optional.of(InvoiceMapper.toDomain(invoiceEntity, itemEntities));
    }

//    @Override
//    public List<Invoice> findAll(UUID customerId) {
//        List<InvoiceEntity> invoiceEntityList = invoiceJpaRepository.findAllByCustomerId(customerId);;
//        return invoiceEntityList.stream()
//                .map(invoiceEntity -> InvoiceMapper.toDomain(invoiceEntity, invoiceItemJpaRepository.findByInvoiceId(invoiceEntity.getId())))
//                .collect(Collectors.toList());
//    }

    @Override
    public List<Invoice> findAll(UUID customerId) {
        List<InvoiceEntity> invoiceEntityList = invoiceJpaRepository.findAllByCustomerId(customerId);

        return invoiceEntityList.stream()
                .map(invoiceEntity -> {
                    // Lấy tất cả InvoiceItemEntity liên quan đến hóa đơn này
                    List<InvoiceItemEntity> itemEntities = invoiceItemJpaRepository.findByInvoiceId(invoiceEntity.getId());

                    // Map entity sang domain
                    return InvoiceMapper.toDomain(invoiceEntity, itemEntities);
                })
                .collect(Collectors.toList());
    }

}
