package com.poly.customerapplication.dto;

import com.poly.customerdomain.model.valueobject.Address;
import com.poly.customerdomain.model.valueobject.Name;
import com.poly.customerdomain.model.valueobject.Nationality;
import com.poly.domain.valueobject.CustomerId;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateCustomerCommand {
    private CustomerId id;
    private Name name;
    private Address address;
    private LocalDate dateOfBirth;
    private Nationality nationality;
}