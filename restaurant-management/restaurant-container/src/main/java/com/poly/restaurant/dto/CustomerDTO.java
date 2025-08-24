package com.poly.restaurant.dto;

public record CustomerDTO(
    String customerId,
    String name,
    String email,
    String phone,
    String roomNumber,
    String hotelId
) {}
