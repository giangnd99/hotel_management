package com.poly.booking.management.domain.entity;

import com.poly.domain.valueobject.Money;

import java.util.List;

public class ServiceManagement {

    private List<Service> service;
    private Money totalCost;
    private boolean isAvailable;


    public ServiceManagement(List<Service> service) {
        this.service = service;
    }

    public List<Service> getService() {
        return service;
    }
}
