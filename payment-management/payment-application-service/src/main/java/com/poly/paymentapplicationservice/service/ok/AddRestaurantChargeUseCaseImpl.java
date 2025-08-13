//package com.poly.paymentapplicationservice.service.ok;
//
//import com.poly.domain.valueobject.RestaurantId;
//import com.poly.paymentapplicationservice.dto.command.ok.AddChargeToInvoiceCommand;
//import com.poly.paymentapplicationservice.dto.result.ChargeResult;
//import com.poly.paymentapplicationservice.exception.ApplicationServiceException;
//import com.poly.paymentapplicationservice.port.input.ok.AddRestaurantChargeUseCase;
//import com.poly.paymentdomain.model.entity.value_object.Money;
//import com.poly.paymentdomain.model.entity.value_object.Quantity;
//
//import java.util.Optional;
//
//public class AddRestaurantChargeUseCaseImpl implements AddRestaurantChargeUseCase {
//
//    private final InvoiceBookingRepository invoiceBookingRepository;
//
//    private final InvoiceRestaurantRepository invoiceRestaurantRepository;
//
//    public AddRestaurantChargeUseCaseImpl(InvoiceBookingRepository invoiceBookingRepository, InvoiceRestaurantRepository invoiceRestaurantRepository) {
//        this.invoiceBookingRepository = invoiceBookingRepository;
//        this.invoiceRestaurantRepository = invoiceRestaurantRepository;
//    }
//
//    @Override
//    public ChargeResult addRestaurant(AddChargeToInvoiceCommand cmd) {
//
//        Optional<InvoiceBooking> invoiceBooking = invoiceBookingRepository.findByBookingId(cmd.getBookingId());
//
//        if(invoiceBooking.isEmpty()){
//            throw new ApplicationServiceException("Invoice booking not found");
//        }
//
//        InvoiceRestaurant invoiceRestaurant = InvoiceRestaurant.builder()
//                .id(InvoiceRestaurantId.generate())
//                .restaurantId(RestaurantId.from(cmd.getServiceChargeId()))
//                .invoiceBookingId(InvoiceBookingId.from(cmd.getBookingId()))
//                .restaurantName(cmd.getNameRestaurant())
//                .quantity(Quantity.from(cmd.getQuantity()))
//                .unitPrice(Money.from(cmd.getUnitPrice()))
//                .build();
//
//        invoiceRestaurantRepository.save(invoiceRestaurant);
//        ChargeResult result = ChargeResult.builder()
//                .bookingId(invoiceRestaurant.getInvoiceBookingId().getValue())
//                .serviceId(invoiceRestaurant.getRestaurantId().getValue())
//                .status(true)
//                .build();
//
//        return result;
//    }
//}
