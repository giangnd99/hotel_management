package com.poly.restaurant.application.dto;

import java.util.UUID;

public record ReviewDTO(UUID customerId, String rating, String comment) {
}
