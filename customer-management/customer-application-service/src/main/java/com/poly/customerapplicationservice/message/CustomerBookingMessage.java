package com.poly.customerapplicationservice.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerBookingMessage {

    private String customerId;
    private String username;
    private String firstName;
    private String lastName;
    private boolean active;
}
