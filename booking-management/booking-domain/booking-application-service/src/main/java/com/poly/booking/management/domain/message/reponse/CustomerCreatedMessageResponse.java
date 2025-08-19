package com.poly.booking.management.domain.message.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomerCreatedMessageResponse {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
}
