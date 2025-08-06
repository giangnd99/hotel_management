package com.poly.booking.management.domain.entity;

import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.ServiceId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ServiceManagement {

    private List<Service> services;
    private Money totalCost;
    private boolean isAvailable;


    public ServiceManagement(List<Service> services) {
        this.services = services;
    }

    public void setInformationAndPriceService(Booking booking) {

        HashMap<ServiceId, Service> idHashMap = getServiceIdHashMapById();
        Set<ServiceId> availableServices = new HashSet<>();

        booking.getServices().forEach(service -> {
            ServiceId serviceId = service.getId();
            Service serviceInHotel = idHashMap.get(serviceId);

            if (serviceInHotel != null
                    && !availableServices.contains(serviceId)
                    && serviceInHotel.isAvailable()) {

                availableServices.add(service.getId());
                service.updateServiceInformation(
                        serviceInHotel.getServiceName(),
                        serviceInHotel.getTotalCost());
            }
        });


    }

    private HashMap<ServiceId, Service> getServiceIdHashMapById() {
        return services.stream().collect(
                HashMap::new,
                (map, value) -> map.put(value.getId(), value),
                HashMap::putAll);
    }

    public List<Service> getService() {
        return services;
    }
}
