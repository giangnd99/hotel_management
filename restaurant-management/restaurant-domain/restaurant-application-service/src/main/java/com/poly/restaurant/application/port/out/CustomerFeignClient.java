package com.poly.restaurant.application.port.out;

import com.poly.restaurant.application.dto.CustomerDTO;

public interface CustomerFeignClient {
    CustomerDTO getCustomerById(String customerId);
    CustomerDTO getCustomerRoomInfo(String customerId);
}
