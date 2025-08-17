package com.poly.room.management.domain.port.out.publisher.request;

import com.poly.message.model.restaurant.RestaurantRequestMessage;

public interface RestaurantCheckOutRequestPublisher {

    void publish(RestaurantRequestMessage restaurantRequestMessage);
}
