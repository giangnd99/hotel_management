package com.poly.paymentapplicationservice.service.ok;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.domain.valueobject.PaymentMethod;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.domain.valueobject.RestaurantId;
import com.poly.paymentapplicationservice.dto.command.CreatePaymentLinkCommand;
import com.poly.paymentapplicationservice.dto.command.ok.CreatePaymentImmediateCommand;
import com.poly.paymentapplicationservice.dto.command.ok.ItemCommand;
import com.poly.paymentapplicationservice.dto.result.PaymentLinkResult;
import com.poly.paymentapplicationservice.port.input.ok.ProcessDirectPaymentUseCase;
import com.poly.paymentapplicationservice.port.output.PaymentGateway;
import com.poly.paymentapplicationservice.share.CheckoutResponseData;
import com.poly.paymentapplicationservice.share.ItemData;
import com.poly.paymentdomain.model.entity.*;
import com.poly.paymentdomain.model.entity.value_object.*;
import com.poly.paymentdomain.output.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ProcessDirectPaymentUseCase interface
 * 
 * This class handles the creation of immediate payment links for both restaurant and service payments.
 * It creates the necessary entities (Invoice, Payment, InvoicePayment) and integrates with PayOS
 * payment gateway to generate payment links.
 * 
 * Main responsibilities:
 * - Create Invoice entity with calculated totals
 * - Create Payment entity with PayOS method
 * - Create InvoicePayment to link invoice and payment
 * - Create InvoiceRestaurant or InvoiceService based on payment method
 * - Generate payment link via PayOS gateway
 * - Return PaymentLinkResult with payment details
 */
public class ProcessDirectPaymentUseCaseImpl implements ProcessDirectPaymentUseCase {

    // Repository dependencies for data persistence
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final InvoiceRestaurantRepository invoiceRestaurantRepository;
    private final InvoicePaymentRepository invoicePaymentRepository;
    private final InvoiceServiceRepository invoiceServiceRepository;
    // Payment gateway client for external payment processing
    private final PaymentGateway payOSClient;

    /**
     * Constructor with dependency injection
     * 
     * @param invoiceRepository - Repository for Invoice entity operations
     * @param paymentRepository - Repository for Payment entity operations
     * @param invoiceRestaurantRepository - Repository for InvoiceRestaurant entity operations
     * @param invoicePaymentRepository - Repository for InvoicePayment entity operations
     * @param invoiceServiceRepository - Repository for InvoiceService entity operations
     * @param payOsClient - PayOS payment gateway client
     */
    public ProcessDirectPaymentUseCaseImpl(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository, InvoiceRestaurantRepository invoiceRestaurantRepository, InvoicePaymentRepository invoicePaymentRepository, InvoiceServiceRepository invoiceServiceRepository, PaymentGateway payOsClient) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.invoiceRestaurantRepository = invoiceRestaurantRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
        this.invoiceServiceRepository = invoiceServiceRepository;
        this.payOSClient = payOsClient;
    }

    /**
     * Main use case method to create a payment link for immediate payment
     * 
     * This method orchestrates the entire payment link creation process:
     * 1. Creates Invoice with calculated totals (subtotal + tax)
     * 2. Creates Payment with PayOS method and PENDING status
     * 3. Links Invoice and Payment via InvoicePayment
     * 4. Creates InvoiceRestaurant or InvoiceService based on payment method
     * 5. Calls PayOS gateway to generate payment link
     * 6. Returns PaymentLinkResult with payment details
     * 
     * @param command - Contains payment details, items, tax rate, and payment method
     * @return PaymentLinkResult - Contains payment ID, order code, status, and payment link
     * @throws Exception - If any error occurs during the process
     */
    @Override
    public PaymentLinkResult CreatePaymentLinkUseCase(CreatePaymentImmediateCommand command) throws Exception {

        // Step 1: Create Invoice entity with calculated totals
        // - Subtotal: sum of all item amounts
        // - Tax rate: converted from percentage to decimal
        // - Total amount: subtotal + (subtotal * tax rate)
        Invoice invoice = Invoice.builder()
                .invoiceId(InvoiceId.generate())
                .createdBy(StaffId.from(command.getStaff() != null ? command.getStaff() : null))
                .subTotal(Money.from(calculatePaymentSubTotal(command.getItems())))
                .taxRate(Money.from(calculateTaxRate(command.getTaxRate() != null ? command.getTaxRate() : BigDecimal.ZERO)))
                .invoiceStatus(InvoiceStatus.PENDING)
                .totalAmount(Money.from(calculateTotalAmount(calculatePaymentSubTotal(command.getItems()), calculateTaxRate(command.getTaxRate() != null ? command.getTaxRate() : BigDecimal.ZERO))))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Step 2: Create Payment entity
        // - Status: PENDING (waiting for payment completion)
        // - Method: PAYOS (PayOS payment gateway)
        // - Transaction type: SERVICE (for service payments)
        // - Reference code: auto-generated unique identifier
        Payment payment = Payment.builder()
                .paymentId(PaymentId.generate())
                .paymentStatus(PaymentStatus.PENDING)
                .amount(invoice.getTotalAmount())
                .method(PaymentMethod.PAYOS)
                .createdAt(LocalDateTime.now())
                .paymentTransactionType(PaymentTransactionType.SERVICE)
                .referenceCode(PaymentReference.generate())
                .build();

        // Step 3: Create InvoicePayment to link Invoice and Payment
        InvoicePayment invoicePayment = InvoicePayment.builder()
                .id(InvoicePaymentId.generate())
                .invoiceId(invoice.getId())
                .paymentId(payment.getId())
                .build();

        // Step 4: Create InvoiceRestaurant or InvoiceService based on payment method
        // This loop processes each item and creates appropriate invoice line items
        for (ItemCommand itemCommand : command.getItems()) {
            // If payment method is "restaurant", create InvoiceRestaurant
            if (command.getTypeSerivce().equals("restaurant")) {
                InvoiceRestaurant invoiceRestaurant = InvoiceRestaurant.builder()
                        .id(InvoiceRestaurantId.generate())
                        .restaurantId(RestaurantId.from(itemCommand.getId()))
                        .restaurantName(itemCommand.getName())
                        .unitPrice(Money.from(itemCommand.getAmount()))
                        .quantity(Quantity.from(itemCommand.getQuantity()))
                        .build();
                 invoiceRestaurantRepository.save(invoiceRestaurant);
            }

            // If payment method is "service", create InvoiceService
            if (command.getTypeSerivce().equals("service")) {
                InvoiceService invoiceService = InvoiceService.builder()
                        .id(InvoiceServiceId.generate())
                        .serviceId(ServiceId.from(itemCommand.getId()))
                        .serviceName(itemCommand.getName())
                        .unitPrice(Money.from(itemCommand.getAmount()))
                        .quantity(Quantity.from(itemCommand.getQuantity()))
                        .build();
                invoiceServiceRepository.save(invoiceService);
            }
        }

        // Step 5: Persist all entities to database
        // Order matters: Invoice -> Payment -> InvoicePayment
        invoiceRepository.save(invoice);
        paymentRepository.save(payment);
        invoicePaymentRepository.save(invoicePayment);

        // Step 6: Prepare command for PayOS payment gateway
        // Convert domain entities to DTOs expected by PayOS
        CreatePaymentLinkCommand createPaymentLinkCommand = CreatePaymentLinkCommand.builder()
                .referenceCode(payment.getReferenceCode().getValue())
                .amount(
                        payment.getAmount().getValue().setScale(0, RoundingMode.HALF_UP).intValue()
                )
                .items(
                        command.getItems().stream().map(ItemCommand -> ItemData.builder()
                                .name(ItemCommand.getName())
                                .price(ItemCommand.getAmount())
                                .quantity(ItemCommand.getQuantity())
                                .build()).collect(Collectors.toList())
                )
                .description("TTDV- " + payment.getReferenceCode().getValue())
                .build();

        // Step 7: Call PayOS gateway to create payment link
        // This external call returns checkout response with payment link
        CheckoutResponseData paymentLinkResult = payOSClient.createPaymentLink(createPaymentLinkCommand);

        // Step 8: Build and return final result
        // Maps PayOS response to domain result object
        PaymentLinkResult result = PaymentLinkResult.builder()
                .paymentId(payment.getId().getValue())
                .orderCode(createPaymentLinkCommand.getReferenceCode())
                .status(paymentLinkResult.getStatus())
                .paymentLink(paymentLinkResult.getCheckoutUrl())
                .build();

        return result;
    }

    /**
     * Calculate total amount including tax
     * 
     * Formula: total = subtotal + (subtotal * taxRate)
     * 
     * @param subTotal - Base amount before tax
     * @param taxRate - Tax rate as decimal (e.g., 0.1 for 10%)
     * @return Total amount including tax
     */
    private BigDecimal calculateTotalAmount(BigDecimal subTotal, BigDecimal taxRate) {
        BigDecimal rateAmount = subTotal.multiply(taxRate);
        return subTotal.add(rateAmount);
    }

    /**
     * Convert tax rate from percentage to decimal
     * 
     * Example: 10% -> 0.1, 5% -> 0.05
     * 
     * @param taxRate - Tax rate as percentage (e.g., 10 for 10%)
     * @return Tax rate as decimal
     */
    private BigDecimal calculateTaxRate(BigDecimal taxRate) {
        return taxRate.divide(BigDecimal.valueOf(100));
    }

    /**
     * Calculate subtotal by summing all item amounts
     * 
     * Iterates through all items and adds their amounts together
     * 
     * @param itemCommands - List of items with amounts
     * @return Sum of all item amounts
     */
    private BigDecimal calculatePaymentSubTotal(List<ItemCommand> itemCommands) {
        return itemCommands.stream().map(ItemCommand::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
