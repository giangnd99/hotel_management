package com.poly.paymentdataaccess.repository.impl;

//@Repository
//public class InvoiceItemRepository implements com.poly.paymentdomain.output.InvoiceItemRepository {
//
//    private InvoiceItemJpaRepository invoiceItemJpaRepository;
//
//    @Override
//    public InvoiceItem save(InvoiceItem invoiceItem) {
//        var entity = InvoiceItemMapper.toEntity(invoiceItem);
//        return invoiceItemJpaRepository.save(invoiceItem);
//    }
//
//    @Override
//    public InvoiceItem update(InvoiceItem invoiceItem) {
//        return invoiceItemJpaRepository.save(invoiceItem);
//    }
//
//    @Override
//    public void deleteInvoice(Invoice invoice) {
//
//    }
//
//    @Override
//    public List<InvoiceItem> findByInvoiceId(UUID invoiceId) {
//        List<InvoiceItemEntity> entityList = invoiceItemJpaRepository.findByInvoiceId(invoiceId);
//        return entityList.stream().map(InvoiceItemMapper::toDomain).toList();
//    }
//}
