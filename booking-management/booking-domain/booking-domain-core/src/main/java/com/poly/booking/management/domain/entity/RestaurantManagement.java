package com.poly.booking.management.domain.entity;

public class RestaurantManagement {

    private Order order;


    public RestaurantManagement(Order order) {
        this.order = order;
    }

    public void setInformationAndPriceService(Booking booking) {
           Order orderInHotel = order;
           booking.setOrder(orderInHotel);
    }
}
