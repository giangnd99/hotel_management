package com.poly.booking.management.domain.entity;

import com.poly.domain.valueobject.Money;

import java.util.List;

public class ServiceManagement {

    private List<Service> services;
    private Money totalCost;
    private boolean isAvailable;


    public ServiceManagement(List<Service> services) {
        this.services = services;
    }

    public List<Service> getService() {
        return services;
    }
}
