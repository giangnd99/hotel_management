package com.poly.paymentdomain.model.entity;

import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdomain.model.entity.valueobject.*;
import lombok.Getter;

import java.math.BigDecimal;
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
    private Money taxRate;       // Tiền thuế (nếu có)
    private Money discountAmount;  // Mã giảm giá (nếu có)
    private Money totalAmount;     // = subTotal + tax - discount
    private Money paidAmount; // Tổng tiền đã trả / thanh toán
    private InvoiceStatus status; // DRAFT, FINALIZED, PAID, CANCELLED
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private List<InvoiceItem> items = new ArrayList<>(); // Chứa danh sách các dịch vụ, phòng, ...
    private Description note;  // Ghi chú nếu cần, mặc định trống
    private Money changeAmount; // Tiền thối khách đưa dư

    private Invoice(Builder builder) {
        this.setId(builder.invoiceId);
        this.bookingId = builder.bookingId != null ? builder.bookingId : null;
        this.customerId = builder.customerId != null ? builder.customerId : null;
        this.createdBy = builder.createdBy == null ? StaffId.system() : builder.createdBy;
        this.lastUpdatedBy = builder.lastUpdatedBy == null ? StaffId.system() : builder.lastUpdatedBy;
        this.voucherId = builder.voucherId == null ? null : builder.voucherId;
        this.taxRate = builder.taxRate != null ? builder.taxRate : Money.zero();
        this.discountAmount = builder.discountAmount != null ? builder.discountAmount : Money.zero();
        this.paidAmount = builder.paidAmount != null ? builder.paidAmount : Money.zero();
        this.status = builder.status != null ? builder.status : InvoiceStatus.DRAFT;
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = builder.lastUpdatedAt != null ? builder.lastUpdatedAt : LocalDateTime.now();
        this.items = builder.items != null ? builder.items : new ArrayList<>();
        this.note = builder.note != null ? builder.note : Description.empty();
    }

    public static final class Builder {
        private InvoiceId invoiceId;
        private BookingId bookingId;
        private CustomerId customerId;
        private StaffId createdBy;
        private StaffId lastUpdatedBy;
        private VoucherId voucherId;
        private Money taxRate;
        private Money discountAmount;
        private Money paidAmount;
        private InvoiceStatus status;
        private LocalDateTime lastUpdatedAt;
        private List<InvoiceItem> items;
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

        public Builder taxRate(Money val) {
            taxRate = val;
            return this;
        }

        public Builder discountAmount(Money val) {
            discountAmount = val;
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

        public Builder lastUpdatedAt(LocalDateTime val) {
            lastUpdatedAt = val;
            return this;
        }

        public Builder items(List<InvoiceItem> val) {
            items = val;
            return this;
        }

        public Builder note(Description val) {
            note = val;
            return this;
        }

        public Invoice build() {
            Invoice invoice = new Invoice(this);
            invoice.recalculateTotals();
            return invoice;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // -- Functions calculate amount

    public Money calculateSubTotal() {
        return items.stream().map(InvoiceItem::amount).reduce(Money.zero(),(money1 , money2) -> money1.add(money2));
    }

    public Money calculateTotalAmount() {
        Money subTotal = this.subTotal;
        Money tax = subTotal.multiply(this.taxRate.getValue().divide(BigDecimal.valueOf(100)));
        Money discount = this.discountAmount;
        Money paidAmount = this.paidAmount != null ? this.paidAmount : Money.zero();
        return subTotal.add(tax).subtract(discount).subtract(paidAmount);
    }

    public void recalculateTotals() {
        this.subTotal = calculateSubTotal();
        this.totalAmount = calculateTotalAmount();
    }

    public void pay(Money amountPaid) {
        if (this.paidAmount == null) {
            this.paidAmount = amountPaid;
        } else {
            this.paidAmount = this.paidAmount.add(amountPaid);
        }
        recalculateChange();
    }

    private void recalculateChange() {
        if (this.paidAmount != null && this.totalAmount != null) {
            this.changeAmount = this.paidAmount.isGreaterThan(this.totalAmount)
                    ? this.paidAmount.subtract(this.totalAmount)
                    : Money.zero();

            this.status = this.paidAmount.isGreaterThanOrEqualTo(this.totalAmount)
                    ? InvoiceStatus.PAID
                    : InvoiceStatus.PENDING;
        } else {
            this.changeAmount = Money.zero();
            throw new RuntimeException("Tiền khách trả hoặc tổng tiền đang trống.");
        }
    }

    // --- Check Status

    public boolean isPaid() {
        return this.status.equals(InvoiceStatus.PAID);
    }

    public void markAsPaid(LocalDateTime val) {
        this.status = InvoiceStatus.PAID;
        this.lastUpdatedAt = val;
    }

    public void markAsCancel(LocalDateTime val) {
        this.status = InvoiceStatus.CANCELED;
        this.lastUpdatedAt = val;
    }

    // ---- Function add & remote item

    public void addItem(InvoiceItem item) {
        if (items == null) throw new NullPointerException("items cannot be null");
        this.items.add(item);
        recalculateTotals();
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void removeItem(InvoiceItem item) {
        if (items == null) throw new NullPointerException("items cannot be null");
        this.items.remove(item);
        recalculateTotals();
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot cancel a PAID invoice.");
        }
        this.status = InvoiceStatus.CANCELED;
        this.lastUpdatedAt = LocalDateTime.now();
    }
}
