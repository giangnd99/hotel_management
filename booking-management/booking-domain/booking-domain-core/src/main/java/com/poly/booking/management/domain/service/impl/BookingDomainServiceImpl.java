package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.entity.*;
import com.poly.booking.management.domain.event.*;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.DateCustom;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BookingDomainServiceImpl implements BookingDomainService {

    @Override
    public BookingCreatedEvent validateAndInitiateBooking(Booking booking, List<Room> rooms) {

//        validateRestaurantAvailability(booking, order);
//        validateServiceAvailabilityAndGetPrices(booking, service); i
        validateRoomAvailabilityAndGetPrices(booking, rooms);
        booking.updateAndValidateTotalPrice();
        booking.initiateBooking();
        log.info("Booking created with id: {} ", booking.getId().getValue());
        return new BookingCreatedEvent(booking, DateCustom.now());
    }

    @Override
    public DepositCreatedEvent validateAndInitiateDeposit(Deposit deposit, Booking booking) {

        deposit.setCustomerId(booking.getCustomerId());
        deposit.setAmount(booking.getTotalPrice());
        deposit.initDeposit(booking.getId());
        log.info("Deposit created with id: {} ", deposit.getId().getValue());
        return new DepositCreatedEvent(deposit, DateCustom.now());
    }

    @Override
    public DepositPaidEvent payDeposit(Deposit deposit, Booking booking) {
        deposit.payDeposit();
        log.info("Deposit paid with id: {} ", deposit.getId().getValue());
        return new DepositPaidEvent(deposit, DateCustom.now());
    }

    @Override
    public DepositRefundedEvent refundDeposit(Deposit deposit, Booking booking, String reason) {
        deposit.checkRegularityForRefundDeposit();
        deposit.processRefundAfterDeposit(reason);
        deposit.refundDeposit();
        log.info("Deposit refunded with id: {} ", deposit.getId().getValue());
        return new DepositRefundedEvent(deposit, DateCustom.now());
    }

    @Override
    public DepositCancelledEvent cancelPaymentDeposit(Deposit deposit, Booking booking, List<String> failureMessages) {
        deposit.cancelDeposit(failureMessages);
        log.info(" Payment of Deposit cancelled with id: {} ", deposit.getId().getValue());
        return new DepositCancelledEvent(deposit, DateCustom.now());
    }

    @Override
    public BookingCancelledEvent cancelDepositBooking(Booking booking) {
        booking.cancelConfirmedBooking();
        log.info("Deposit of booking cancelled with id: {} ", booking.getId().getValue());
        return null;
    }

    @Override
    public CheckInEvent checkInBooking(Booking booking) {
        booking.checkIn();
        log.info("Booking checked in with id: {} ", booking.getId().getValue());
        return new CheckInEvent(booking, DateCustom.now());
    }

    @Override
    public void updateBooking(Booking booking) {

    }

    @Override
    public BookingDepositEvent confirmDepositBooking(Booking booking) {
        booking.confirmDepositBooking();
        log.info("Booking confirmed with id: {} ", booking.getId().getValue());
        return new BookingDepositEvent(booking, DateCustom.now());
    }

    @Override
    public BookingPaidEvent payBooking(Booking booking) {
        booking.paidBooking();
        log.info("Booking paid with id: {} ", booking.getId().getValue());
        return new BookingPaidEvent(booking, DateCustom.now());
    }

    @Override
    public CheckOutEvent checkOutBooking(Booking booking) {
        booking.checkOut();
        log.info("Booking checked out with id: {} ", booking.getId().getValue());
        return new CheckOutEvent(booking, DateCustom.now());
    }

    @Override
    public BookingCancelledEvent cancelBooking(Booking booking) {
        booking.cancelWhilePaidFailed();
        log.info("Booking cancelled with id: {} ", booking.getId().getValue());
        return new BookingCancelledEvent(booking, DateCustom.now());
    }

    //
//    private void validateRestaurantAvailability(Booking booking, Order order) {
//        RestaurantManagement restaurantManagement = new RestaurantManagement(order);
//        restaurantManagement.setInformationAndPriceService(booking);
//    }
//
//    private void validateServiceAvailabilityAndGetPrices(Booking booking, List<Service> services) {
//        ServiceManagement management = new ServiceManagement(services);
//        management.setInformationAndPriceService(booking);
//    }
//
    private void validateRoomAvailabilityAndGetPrices(Booking booking, List<Room> rooms) {
        RoomManagement management = new RoomManagement(rooms);
        management.setRoomsInformation(booking);
    }
}
