package com.poly.paymentapplicationservice.port.input.ok;

import com.poly.paymentapplicationservice.dto.command.ok.AddChargeToInvoiceCommand;
import com.poly.paymentapplicationservice.dto.result.ChargeResult;

public interface AddRestaurantChargeUseCase {
    ChargeResult addRestaurant (AddChargeToInvoiceCommand cmd);
}
