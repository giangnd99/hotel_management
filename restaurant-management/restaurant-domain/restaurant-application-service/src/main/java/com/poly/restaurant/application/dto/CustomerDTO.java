package com.poly.restaurant.application.dto;

public record CustomerDTO(
    String customerId,
    String name,
    String email,
    String phone,
    String roomNumber,
    String hotelId
) {}
