package com.poly.booking.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.CustomerId;

public class Customer extends BaseEntity<CustomerId>{

    private String name;
    private String email;
}
