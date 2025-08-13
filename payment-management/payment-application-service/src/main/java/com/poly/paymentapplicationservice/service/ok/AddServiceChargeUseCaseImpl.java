//package com.poly.paymentapplicationservice.service.ok;
//
//import com.poly.paymentapplicationservice.dto.command.ok.AddChargeToInvoiceCommand;
//import com.poly.paymentapplicationservice.dto.result.ChargeResult;
//import com.poly.paymentapplicationservice.exception.ApplicationServiceException;
//import com.poly.paymentapplicationservice.port.input.ok.AddServiceChargeUseCase;
//import com.poly.paymentdomain.model.entity.value_object.*;
//
//import java.util.Optional;
//
//public class AddServiceChargeUseCaseImpl implements AddServiceChargeUseCase {
//
//    private final InvoiceBookingRepository invoiceBookingRepository;
//
//    private final InvoiceServiceRepository invoiceServiceRepository;
//
//    public AddServiceChargeUseCaseImpl(InvoiceBookingRepository invoiceBookingRepository, InvoiceServiceRepository invoiceServiceRepository) {
//        this.invoiceBookingRepository = invoiceBookingRepository;
//        this.invoiceServiceRepository = invoiceServiceRepository;
//    }
//
//    @Override
//    public ChargeResult addService(AddChargeToInvoiceCommand cmd) {
//
//        Optional<InvoiceBooking> invoiceBooking = invoiceBookingRepository.findByBookingId(cmd.getBookingId());
//
//        if(invoiceBooking.isEmpty()){
//            throw new ApplicationServiceException("Invoice booking not found");
//        }
//
//        InvoiceService invoiceService = InvoiceService.builder()
//                .id(InvoiceServiceId.generate())
//                .serviceId(ServiceId.from(cmd.getServiceChargeId()))
//                .invoiceBookingId(InvoiceBookingId.from(cmd.getBookingId()))
//                .serviceName(cmd.getNameRestaurant())
//                .quantity(Quantity.from(cmd.getQuantity()))
//                .unitPrice(Money.from(cmd.getUnitPrice()))
//                .build();
//
//        invoiceServiceRepository.save(invoiceService);
//
//        ChargeResult result = ChargeResult.builder()
//                .bookingId(invoiceService.getInvoiceBookingId().getValue())
//                .serviceId(invoiceService.getServiceId().getValue())
//                .status(true)
//                .build();
//
//        return result;
//    }
//}
