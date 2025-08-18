package com.poly.servicemanagement.service;

import com.poly.servicemanagement.entity.Service_;

import java.util.List;
import java.util.Optional;

public interface Service_Service {

    // CRUD methods
    List<Service_> getAll();
    Optional<Service_> getById(Integer id);
    Service_ create(Service_ service);
    Service_ update(Integer id, Service_ service);
    boolean delete(Integer id);

    // New Dashboard & Statistics methods
    Double getServiceRevenue();

    // Search & Filter methods
    List<Service_> searchServices(String name, String description, String category, Double minPrice, Double maxPrice);
    List<Service_> filterByDepartment(String department);
    List<Service_> filterByState(String state);

    // Category management
    List<String> getAllCategories();
    List<Service_> getServicesByCategory(String category);

    // Pricing management
    Object getPriceRange();
    Service_ updatePrice(Integer id, Double newPrice);

    // Availability management
    List<Service_> getAvailableServices();
    Service_ updateAvailability(Integer id, Boolean available);

    // Statistics
    Object getUsageStatistics();
    Object getRevenueStatistics();
}