package com.poly.customerapplicationservice.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String street;
    private String ward;
    private String district;
    private String city;
}
