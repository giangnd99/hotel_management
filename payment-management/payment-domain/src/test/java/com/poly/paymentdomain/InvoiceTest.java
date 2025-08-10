package com.poly.paymentdomain;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.InvoiceBooking;
import com.poly.paymentdomain.model.entity.value_object.*;
import com.poly.paymentdomain.model.entity.valueobject2.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Invoice domain unit test")
class InvoiceTest {

    private final Money tax = Money.from(BigDecimal.valueOf(0.08));

    private List<InvoiceBooking> itemList = Arrays.asList(
            InvoiceBooking.builder()
                    .serviceId(ServiceId.from(UUID.randomUUID()))
                    .description(Description.from("Test Invoice 1"))
                    .serviceType(new ServiceType(ServiceType.Status.FOOD))
                    .quantity(Quantity.from(1))
                    .unitPrice(Money.from(BigDecimal.valueOf(100000)))
                    .usedAt(LocalDateTime.now())
                    .note(Description.from("Khách khó tính như quỷ"))
                    .build(),
            InvoiceBooking.builder()
                    .serviceId(ServiceId.from(UUID.randomUUID()))
                    .description(Description.from("Test Invoice 2"))
                    .serviceType(new ServiceType(ServiceType.Status.FOOD))
                    .quantity(Quantity.from(1))
                    .unitPrice(Money.from(BigDecimal.valueOf(100000)))
                    .usedAt(LocalDateTime.now())
                    .note(Description.from("Khách khó tính như quỷ"))
                    .build(),
            InvoiceBooking.builder()
                    .serviceId(ServiceId.from(UUID.randomUUID()))
                    .description(Description.from("Test Invoice 3"))
                    .serviceType(new ServiceType(ServiceType.Status.FOOD))
                    .quantity(Quantity.from(1))
                    .unitPrice(Money.from(BigDecimal.valueOf(100000)))
                    .usedAt(LocalDateTime.now())
                    .note(Description.from("Khách khó tính như quỷ"))
                    .build()
    );

    private Invoice sampleInvoice(List<InvoiceBooking> items) {
        return Invoice.builder()
                .id(InvoiceId.generate())
                .bookingId(BookingId.from(UUID.randomUUID()))
                .customerId(CustomerId.fromValue(UUID.randomUUID()))
                .createdBy(StaffId.from(UUID.randomUUID()))
                .taxRate(tax) // 8%
                .discountAmount(Money.from(BigDecimal.valueOf(10000)))
                .items(items)
                .build();
    }

    public InvoiceBooking sampleInvoiceItem(UUID serviceId, String description, ServiceType serviceType, Integer quantity, BigDecimal unitPrice, String note) {
        return InvoiceBooking.builder()
                .serviceId(ServiceId.from(serviceId))
                .description(Description.from(description))
                .serviceType(serviceType)
                .quantity(Quantity.from(quantity))
                .unitPrice(Money.from(unitPrice))
                .usedAt(LocalDateTime.now())
                .note(Description.from(note))
                .build();
    }

    @Test
    void createInvoice_withItems_shouldCalculateTotalCorrectly() {
        // given
        var invoice = sampleInvoice(itemList);

        // when
        var subTotal = invoice.getSubTotal();
        var total = invoice.getTotalAmount();

        // then
        assertThat(subTotal.getValue()).usingComparator(BigDecimal::compareTo).isEqualTo(BigDecimal.valueOf(300000));
        assertThat(total.getValue()).usingComparator(BigDecimal::compareTo).isEqualTo(BigDecimal.valueOf(314000));
    }

    @Test
    void shouldAddItem_ShouldUpdateSubTotalAndTotal() {
        List <InvoiceBooking> items = new ArrayList<InvoiceBooking>();
        var invoice = sampleInvoice(items);
        invoice.addItem(
                sampleInvoiceItem(UUID.randomUUID(),
                        "Des test 1",
                        new ServiceType(ServiceType.Status.FOOD),
                        Integer.valueOf(1),
                        BigDecimal.valueOf(250000),
                        Description.empty().getValue()
                        ));

        assertThat(invoice.getSubTotal().getValue()).isEqualTo(Money.from(BigDecimal.valueOf(250000)).getValue());
        assertThat(invoice.getTotalAmount().getValue()).isGreaterThan(Money.from(BigDecimal.valueOf(10000)).getValue());
    }

    @Test
    void removeItem_shouldUpdateSubTotalAndTotal() {
        List <InvoiceBooking> items = new ArrayList<InvoiceBooking>();
        var invoice = sampleInvoice(items);
        var item1 = sampleInvoiceItem(UUID.randomUUID(),
                "Des test 1",
                new ServiceType(ServiceType.Status.FOOD),
                Integer.valueOf(1),
                BigDecimal.valueOf(250000),
                Description.empty().getValue()
        );
        var item2 = sampleInvoiceItem(UUID.randomUUID(),
                "Des test 2",
                new ServiceType(ServiceType.Status.FOOD),
                Integer.valueOf(1),
                BigDecimal.valueOf(250000),
                Description.empty().getValue()
        );
        invoice.addItem(item1);
        invoice.addItem(item2);

        invoice.removeItem(item1);
        assertThat(invoice.getSubTotal().getValue()).isEqualTo(Money.from(BigDecimal.valueOf(250000)).getValue());
    }

    @Test
    void payWithExactAmount_shouldHaveZeroChange() {
        List <InvoiceBooking> items = new ArrayList<InvoiceBooking>();
        var invoice = sampleInvoice(items);
        var item1 = sampleInvoiceItem(UUID.randomUUID(),
                "Des test 1",
                new ServiceType(ServiceType.Status.FOOD),
                Integer.valueOf(1),
                BigDecimal.valueOf(250000),
                Description.empty().getValue()
        );
        invoice.addItem(item1);
        invoice.pay(invoice.getTotalAmount());

        assertThat(invoice.getChangeAmount().getValue()).isEqualTo(Money.zero().getValue());
        assertThat(invoice.getStatus().getValue()).isEqualTo(InvoiceStatus.PAID.getValue());
    }

    @Test
    void payWithExcessAmount_shouldCalculateChange() {
        List <InvoiceBooking> items = new ArrayList<InvoiceBooking>();
        var invoice = sampleInvoice(items);
        var item1 = sampleInvoiceItem(UUID.randomUUID(),
                "Des test 1",
                new ServiceType(ServiceType.Status.FOOD),
                Integer.valueOf(1),
                BigDecimal.valueOf(250000),
                Description.empty().getValue()
        );
        invoice.addItem(item1);

        var total = invoice.getTotalAmount();
        invoice.pay(Money.from(BigDecimal.valueOf(300000)));


        assertThat(invoice.getChangeAmount().getValue().setScale(0))
                .isEqualTo(new BigDecimal("40000"));

    }

    @Test
    void payWithInsufficientAmount_shouldNotMarkAsPaid() {
        List <InvoiceBooking> items = new ArrayList<InvoiceBooking>();
        var invoice = sampleInvoice(items);
        var item1 = sampleInvoiceItem(UUID.randomUUID(),
                "Des test 1",
                new ServiceType(ServiceType.Status.FOOD),
                Integer.valueOf(1),
                BigDecimal.valueOf(250000),
                Description.empty().getValue()
        );
        invoice.addItem(item1);
        invoice.pay(Money.from(BigDecimal.valueOf(70000)));

        assertThat(invoice.getStatus()).isNotEqualTo(InvoiceStatus.PAID);
        assertThat(invoice.getPaidAmount().getValue()).isEqualTo(Money.from(BigDecimal.valueOf(70000)).getValue());
    }

    @Test
    void paymentWithTotalAmountNull() {
        var invoice = sampleInvoice(List.of());
        invoice.pay(Money.from(BigDecimal.valueOf(70000)));

        assertThat(invoice.getStatus().getValue()).isEqualTo(InvoiceStatus.PAID.getValue());
    }

}
