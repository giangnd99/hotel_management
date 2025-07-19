package com.poly.restaurant.application.dto;

public record ReviewDTO(Long customerId, int rating, String comment) {
}
