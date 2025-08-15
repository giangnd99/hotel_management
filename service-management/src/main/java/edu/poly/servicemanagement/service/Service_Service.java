package edu.poly.servicemanagement.service;

import edu.poly.servicemanagement.entity.Service_;

import java.util.List;
import java.util.Optional;

public interface Service_Service {
    List<Service_> getAll();

    Optional<Service_> getById(Integer id);

    Service_ create(Service_ service);

    Service_ update(Integer id, Service_ serviceDetails);

    boolean delete(Integer id);

}
