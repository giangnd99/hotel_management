package com.poly.paymentdomain.model.entity;

import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdomain.model.entity.valueobject.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Invoice extends AggregateRoot<InvoiceId> {
    private BookingId bookingId; // Mã Phòng
    private CustomerId customerId; // Mã khách hàng
    private StaffId createdBy; // Nhân viên tạo, mặc định hệ thống tạo là SYSTEM
    private StaffId lastUpdatedBy; // Update bởi nhân viên nào
    private VoucherId voucherId;   // Nếu có mã khuyến mãi liên kết
    private Money subTotal;        // Tổng các InvoiceItem trước khi tính thuế/giảm
    private Money taxAmount;       // Tiền thuế (nếu có)
    private Money discountAmount;  // Mã giảm giá (nếu có)
    private Money totalAmount;     // = subTotal + tax - discount
    private Money paidAmount; // Tổng tiền đã trả / thanh toán
    private InvoiceStatus status; // DRAFT, FINALIZED, PAID, CANCELLED
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private List<InvoiceItem> items = new ArrayList<>(); // Chứa danh sách các dịch vụ, phòng, ...
    private List<Payment> payments = new ArrayList<>(); // Chứa danh sách thanh toán nếu có 2 loại tiền mặt, ck
    private Description note;  // Ghi chú nếu cần, mặc định trống

    private Invoice(Builder builder) {
        this.setId(builder.invoiceId);
        bookingId = builder.bookingId;
        customerId = builder.customerId;
        createdBy = builder.createdBy == null ? StaffId.system() : builder.createdBy;
        lastUpdatedBy = builder.lastUpdatedBy;
        voucherId = builder.voucherId;
        subTotal = builder.subTotal;
        taxAmount = builder.taxAmount;
        discountAmount = builder.discountAmount;
        totalAmount = builder.totalAmount;
        paidAmount = builder.paidAmount;
        status = builder.status ;
        createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        lastUpdatedAt = builder.lastUpdatedAt != null ? builder.lastUpdatedAt : LocalDateTime.now();
        items = builder.items != null ? builder.items : new ArrayList<>();
        payments = builder.payments != null ? builder.payments : new ArrayList<>();
        note = builder.note == null ? Description.from("Nothing any where." ): builder.note;
    }

    public static final class Builder {
        private InvoiceId invoiceId;
        private BookingId bookingId;
        private CustomerId customerId;
        private StaffId createdBy;
        private StaffId lastUpdatedBy;
        private VoucherId voucherId;
        private Money subTotal;
        private Money taxAmount;
        private Money discountAmount;
        private Money totalAmount;
        private Money paidAmount;
        private InvoiceStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime lastUpdatedAt;
        private List<InvoiceItem> items;
        private List<Payment> payments;
        private Description note;

        public Builder() {
        }

        public Builder id(InvoiceId val) {
            invoiceId = val;
            return this;
        }

        public Builder bookingId(BookingId val) {
            bookingId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder createdBy(StaffId val) {
            createdBy = val;
            return this;
        }

        public Builder lastUpdatedBy(StaffId val) {
            lastUpdatedBy = val;
            return this;
        }

        public Builder voucherId(VoucherId val) {
            voucherId = val;
            return this;
        }

        public Builder subTotal(Money val) {
            subTotal = val;
            return this;
        }

        public Builder taxAmount(Money val) {
            taxAmount = val;
            return this;
        }

        public Builder discountAmount(Money val) {
            discountAmount = val;
            return this;
        }

        public Builder totalAmount(Money val) {
            totalAmount = val;
            return this;
        }

        public Builder paidAmount(Money val) {
            paidAmount = val;
            return this;
        }

        public Builder status(InvoiceStatus val) {
            status = val;
            return this;
        }

        public Builder createdAt(LocalDateTime val) {
            createdAt = val;
            return this;
        }

        public Builder lastUpdatedAt(LocalDateTime val) {
            lastUpdatedAt = val;
            return this;
        }

        public Builder items(List<InvoiceItem> val) {
            items = val;
            return this;
        }

        public Builder payments(List<Payment> val) {
            payments = val;
            return this;
        }

        public Builder note(Description val) {
            note = val;
            return this;
        }

        public Invoice build() {
            return new Invoice(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Money calculateSubTotal() {
        return items.stream().map(InvoiceItem::amount).reduce(Money.zero(),(money1 , money2) -> money1.add(money2));
    }

    public Money calculateTotalAmount() {
        return calculateSubTotal()
                .add((this.taxAmount != null) ? calculateSubTotal().multiply(this.taxAmount.getValue()) : Money.zero())
                .subtract((this.discountAmount != null) ? calculateSubTotal().subtract(this.discountAmount) : Money.zero());
    }

    public Money calculatePaidAmount() {
        return payments.stream().map(Payment::amount).reduce(Money.zero(), ((money1, money2) -> money1.add(money2)));
    }

    public void recalculateTotals() {
        this.subTotal = calculateSubTotal();
        this.totalAmount = calculateTotalAmount();
        this.paidAmount = calculatePaidAmount();
    }
}
