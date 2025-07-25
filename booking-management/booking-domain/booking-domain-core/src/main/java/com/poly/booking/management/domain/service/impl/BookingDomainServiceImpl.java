package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.entity.*;
import com.poly.booking.management.domain.event.*;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.Money;

import java.util.List;

public class BookingDomainServiceImpl implements BookingDomainService {

    @Override
    public BookingCreatedEvent validateAndInitiateBooking(Booking booking, Order order, List<Service> service , List<Room> rooms) {


        return null;
    }

    @Override
    public DepositCreatedEvent validateAndInitiateDeposit(Deposit deposit, Booking booking) {
        return null;
    }

    @Override
    public DepositPaidEvent payDeposit(Deposit deposit, Booking booking) {
        return null;
    }

    @Override
    public DepositRefundedEvent refundDeposit(Deposit deposit, Booking booking) {
        return null;
    }

    @Override
    public DepositCancelledEvent cancelPaymentDeposit(Deposit deposit, Booking booking) {
        return null;
    }

    @Override
    public BookingCancelledEvent cancelDepositBooking(Booking booking) {
        return null;
    }

    @Override
    public CheckInEvent checkInBooking(Booking booking) {
        return null;
    }

    @Override
    public void updateBooking(Booking booking) {

    }

    @Override
    public BookingPaidEvent payBooking(Booking booking) {
        return null;
    }

    @Override
    public CheckOutEvent checkOutBooking(Booking booking) {
        return null;
    }

    @Override
    public void cancelBooking(Booking booking) {

    }

    private void validateRestaurantAvailability(Order order){
        RestaurantManagement restaurantManagement = new RestaurantManagement(order);
    }

    private void validateServiceAvailability(List<Service> services){
        ServiceManagement management = new ServiceManagement(services);

    }

    private void validateRoomAvailabilityAndGetPrices(Booking booking, List<Room> rooms){
        RoomManagement management = new RoomManagement(rooms);
        management.setRoomsInformation(booking);
    }
}
