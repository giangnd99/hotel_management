package com.poly.booking.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.ServiceId;

public class Service extends BaseEntity<ServiceId> {

    private String serviceName;
    private Money totalCost;
    private boolean isAvailable;

    public Service(ServiceId serviceId, String serviceName, Money totalCost, boolean isAvailable) {
        super.setId(serviceId);
        this.serviceName = serviceName;
        this.totalCost = totalCost;
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void updateServiceInformation(String serviceName, Money totalCost) {
        this.serviceName = serviceName;
        this.totalCost = totalCost;
    }

    public Money getTotalCost() {
        return this.totalCost;
    }
    public String getServiceName() {
        return this.serviceName;
    }
}
