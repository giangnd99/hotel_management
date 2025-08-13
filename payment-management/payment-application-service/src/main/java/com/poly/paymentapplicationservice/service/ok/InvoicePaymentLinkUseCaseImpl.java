//package com.poly.paymentapplicationservice.service.ok;
//
//import com.poly.paymentapplicationservice.dto.command.CreatePaymentLinkCommand;
//import com.poly.paymentapplicationservice.dto.command.ok.CreateInvoicePaymentCommand;
//import com.poly.paymentapplicationservice.dto.result.PaymentLinkResult;
//import com.poly.paymentapplicationservice.exception.ApplicationServiceException;
//import com.poly.paymentapplicationservice.port.input.ok.InvoicePaymentLinkUseCase;
//import com.poly.paymentapplicationservice.port.output.PaymentGateway;
//import com.poly.paymentapplicationservice.share.CheckoutResponseData;
//import com.poly.paymentapplicationservice.share.ItemData;
//import com.poly.paymentdomain.model.entity.*;
//import com.poly.paymentdomain.output.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//public class InvoicePaymentLinkUseCaseImpl implements InvoicePaymentLinkUseCase {
//
//    private final InvoiceRepository invoiceRepository;
//
//    private final PaymentRepository paymentRepository;
//
//    private final InvoicePaymentRepository invoicePaymentRepository;
//
//    private final InvoiceRestaurantRepository invoiceRestaurantRepository;
//
//    private final InvoiceBookingRepository invoiceBookingRepository;
//
//    private final InvoiceServiceRepository invoiceServiceRepository;
//
//    private final InvoiceVoucherRepository invoiceVoucherRepository;
//
//    private final PaymentGateway payOSClient;
//
//    public InvoicePaymentLinkUseCaseImpl(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository, InvoicePaymentRepository invoicePaymentRepository, InvoiceRestaurantRepository invoiceRestaurantRepository, InvoiceBookingRepository invoiceBookingRepository, InvoiceServiceRepository invoiceServiceRepository, InvoiceVoucherRepository invoiceVoucherRepository, PaymentGateway payOSClient) {
//        this.invoiceRepository = invoiceRepository;
//        this.paymentRepository = paymentRepository;
//        this.invoicePaymentRepository = invoicePaymentRepository;
//        this.invoiceRestaurantRepository = invoiceRestaurantRepository;
//        this.invoiceBookingRepository = invoiceBookingRepository;
//        this.invoiceServiceRepository = invoiceServiceRepository;
//        this.invoiceVoucherRepository = invoiceVoucherRepository;
//        this.payOSClient = payOSClient;
//    }
//
//    @Override
//    public PaymentLinkResult CreatePaymentLinkUseCase(CreateInvoicePaymentCommand command) throws Exception {
//
//        Optional<Invoice> invoice = invoiceRepository.findById(command.getInvoiceId());
//
//        if (invoice.isEmpty()) throw new ApplicationServiceException("Invoice not found");
//
//        Optional<InvoicePayment> invoicePayment = invoicePaymentRepository.findByInvoiceId(command.getInvoiceId());
//
//        Optional<InvoiceBooking> invoiceBooking = invoiceBookingRepository.findByInvoiceId(invoicePayment.get().getPaymentId().getValue());
//
//        Optional<Payment> payment = paymentRepository.findById(invoicePayment.get().getPaymentId().getValue());
//
//        List<InvoiceRestaurant> invoiceRestaurantList = invoiceRestaurantRepository.findAllByBookingId(invoiceBooking.get().getBookingId().getValue());
//
//        List<InvoiceService> invoiceServiceList = invoiceServiceRepository.findAllByBookingId(invoiceBooking.get().getBookingId().getValue());
//
//        List<ItemData> combinedItems = new ArrayList<>();
//
//        // Thêm các phần tử từ InvoiceRestaurant
//        combinedItems.addAll(invoiceRestaurantList.stream()
//                .map(invoiceRestaurant -> ItemData.builder()
//                        .name(invoiceRestaurant.getRestaurantName())
//                        .price(invoiceRestaurant.getUnitPrice().getValue())
//                        .quantity(invoiceRestaurant.getQuantity().getValue())
//                        .build())
//                .collect(Collectors.toList()));
//
//        // Thêm các phần tử từ InvoiceService
//        combinedItems.addAll(invoiceServiceList.stream()
//                .map(invoiceService ->  ItemData.builder()
//                        .name(invoiceService.getServiceName())
//                        .price(invoiceService.getUnitPrice().getValue())
//                        .quantity(invoiceService.getQuantity().getValue())
//                        .build())
//                .collect(Collectors.toList()));
//
//        CreatePaymentLinkCommand createPaymentLinkCommand = CreatePaymentLinkCommand.builder()
//                .referenceCode(payment.get().getReferenceCode().getValue())
//                .amount(Integer.valueOf(payment.get().getAmount().toString()))
//                .items(
//                        combinedItems.stream().map(itemCommand -> ItemData.builder()
//                                        .name(itemCommand.getName())
//                                        .price(itemCommand.getPrice())
//                                        .quantity(itemCommand.getQuantity())
//                                        .build())
//                                .collect(Collectors.toList())
//                )
//                .description("TTHD- " + payment.get().getReferenceCode().getValue())
//                .build();
//
//        CheckoutResponseData paymentLinkResult = payOSClient.createPaymentLink(createPaymentLinkCommand);
//
//        PaymentLinkResult result = PaymentLinkResult.builder()
//                .paymentId(payment.get().getId().getValue())
//                .orderCode(createPaymentLinkCommand.getReferenceCode())
//                .status(paymentLinkResult.getStatus())
//                .paymentLink(paymentLinkResult.getPaymentLinkId())
//                .build();
//
//        return result;
//    }
//}
