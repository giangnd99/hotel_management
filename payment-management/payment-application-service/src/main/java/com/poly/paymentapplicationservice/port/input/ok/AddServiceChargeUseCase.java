package com.poly.paymentapplicationservice.port.input.ok;

import com.poly.paymentapplicationservice.dto.command.ok.AddChargeToInvoiceCommand;
import com.poly.paymentapplicationservice.dto.result.ChargeResult;

public interface AddServiceChargeUseCase {
    ChargeResult addService(AddChargeToInvoiceCommand cmd);
}
