package com.poly.booking.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.ServiceId;

public class Service extends BaseEntity<ServiceId> {

    private String serviceName;
    private Money totalCost;
}
