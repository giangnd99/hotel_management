package com.poly.paymentdomain.model.exception.invoice;

import com.poly.paymentdomain.model.exception.DomainException;

public class InvoiceNotFoundException extends DomainException {
    public InvoiceNotFoundException() {
        super("Hóa đơn không tồn tại.");
    }

}
