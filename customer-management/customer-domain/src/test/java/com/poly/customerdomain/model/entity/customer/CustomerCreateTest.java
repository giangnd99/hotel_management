package com.poly.customerdomain.model.entity.customer;

import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.customerdomain.model.exception.*;
import com.poly.domain.valueobject.CustomerId;
import com.poly.domain.valueobject.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerCreateTest {

    private final CustomerId customerId = new CustomerId(UUID.randomUUID());

    @Test
    void shouldCreateCustomerWithAllFields() {
        // 1.Arrange - 2. Action
        BehaviorData behavior = new BehaviorData("Deluxe", "BREAKFAST");

        Customer customer = Customer.builder()
                .customerId(customerId)
                .userId(UserId.from(UUID.randomUUID()))
                .name(Name.from("Nguyen", "Van A"))
                .address(Address.from("12 Lê Lợi", "Phường 4", "Quận 10", "TP.HCM"))
                .dateOfBirth(DateOfBirth.from(LocalDate.parse("1980-01-01")))
                .level(Level.NONE)
                .image(ImageUrl.from("https://res.cloudinary.com/dhbjvvn87/image/upload/v1751976713/ldbper0tbr5zituky1zi.webp"))
                .behaviorData(behavior)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        // 3.Assert
        assertNotNull(customer.getId());
        assertEquals("Nguyen Van A", Name.from("Nguyen", "Van A"));
   }

    @Test
    void shouldUseDefaultValuesWhenOptionalFieldsNull() {
        // 1.Arrange - 2. Action
        Name name = new Name("Unnamed", "Unnamed");

        Customer customer = Customer.builder()
                .customerId(customerId)
                .userId(UserId.from(UUID.randomUUID()))
                .name(Name.empty())
                .address(Address.empty())
                .dateOfBirth(DateOfBirth.empty())
                .level(Level.NONE)
                .image(ImageUrl.empty())
                .behaviorData(BehaviorData.empty())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        // 3.Assert
        assertNotNull(customer.getId());
        assertEquals("Unnamed Unnamed", name.getFullName());
    }

    @Test
    void shouldThrowExceptionWhenCustomerImageIsNull() {
        // 1.Arrange
        String imgageUrlValue = null;
        // 2.Action
        InvalidCustomerImageException exception = assertThrows(InvalidCustomerImageException.class, () -> {
            ImageUrl imageUrl = ImageUrl.from(imgageUrlValue);
        });
        // 3.Assert
        assertEquals("Ảnh không hợp lệ hoặc đang trống.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCustomerImageIsBlank() {
        // 1.Arrange
        String imgageUrlValue = "";
        // 2.Action
        InvalidCustomerImageException exception = assertThrows(InvalidCustomerImageException.class, () -> {
            ImageUrl imageUrl = ImageUrl.from(imgageUrlValue);
        });
        // 3.Assert
        assertEquals("Ảnh không hợp lệ hoặc đang trống.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAllAddressFieldsAreNull() {
        // 1.Arrange
        String addressValue = null;
        // 2.Action
        InvalidCustomerAddressException exception = assertThrows(InvalidCustomerAddressException.class, () -> {
            Address address =  new Address(addressValue, addressValue, addressValue, addressValue);
        });
        // 3.Assert
        assertEquals("Thông tin địa chỉ không hơp lệ.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAllAddressFieldsAreBlank() {
        // 1.Arrange
        String addressValue = "";
        // 2.Action
        InvalidCustomerAddressException exception = assertThrows(InvalidCustomerAddressException.class, () -> {
            Address address =  new Address(addressValue, addressValue, addressValue, addressValue);
        });
        // 3.Assert
        assertEquals("Thông tin địa chỉ không hơp lệ.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNameIsNull() {
        // 1.Arrange
        String firstNameValue = null;
        String lastNameValue = null;
        // 2.Action
        InvalidCustomerNameException exception = assertThrows(
                InvalidCustomerNameException.class,
                () -> new Name(firstNameValue, lastNameValue)
        );
        // 3.Assert
        assertEquals("Họ tên khách hàng không hợp lệ.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNameIsBlank() {
        // 1.Arrange
        String firstNameValue = "";
        String lastNameValue = "";
        // 2.Action
        InvalidCustomerNameException exception = assertThrows(
                InvalidCustomerNameException.class,
                () -> new Name(firstNameValue, lastNameValue)
        );
        // 3.Assert
        assertEquals("Họ tên khách hàng không hợp lệ.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsNull() {
        // 1.Arrange
        UUID userIdValue = null;
        // 2.Action
        InvalidCustomerUserIdException exception = assertThrows(InvalidCustomerUserIdException.class, () -> {
            UserId userId = new UserId(userIdValue);
        });
        // 3.Assert
        assertEquals("User Id dùng không hợp lệ.", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenNameIsTooShort() {
        // Act & Assert
        CustomerNameLengthOutOfRangeException exception = assertThrows(
                CustomerNameLengthOutOfRangeException.class,
                () -> new Name("Nguy", "a")
        );
        // Optionally: Check context notice error
        assertEquals("Tên khách hàng phải có độ dài từ 5 đến 100 ký tự.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsTooLong() {
        // Arrange
        String tooLongName = "a".repeat(101);
        // Act & Assert
        CustomerNameLengthOutOfRangeException exception = assertThrows(
                CustomerNameLengthOutOfRangeException.class,
                () -> new Name(tooLongName, "a")
        );
        // Optionally: Check context notice error
        assertEquals("Tên khách hàng phải có độ dài từ 5 đến 100 ký tự.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddressIsTooShort() {
        // Act & Assert
        CustomerAddressLengthOutOfRangeException exception = assertThrows(
                CustomerAddressLengthOutOfRangeException.class,
                () -> new Address("", "test", "test", "test")
        );
        // Optionally: Check context notice error
        assertEquals("Địa chỉ khách hàng phải có độ dài từ 5 đến 100 ký tự.",  exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddressIsTooLong() {
        // Arrange
        String addressToLong = "a".repeat(101);
        // Act & Assert
        CustomerAddressLengthOutOfRangeException exception = assertThrows(
                CustomerAddressLengthOutOfRangeException.class,
                () -> new Address(addressToLong, "test", "test", "test")
        );
        // Optionally: Check context notice error
        assertEquals("Địa chỉ khách hàng phải có độ dài từ 5 đến 100 ký tự.",  exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBirthTooLong() {
        // Act & Assert
        CustomerAgeOutOfRangeException exception = assertThrows(
                CustomerAgeOutOfRangeException.class,
                () -> new DateOfBirth(LocalDate.of(1080, 1, 1))
        );
        // Optionally: Check context notice error
        assertEquals("Tuổi phải nằm trong khoảng từ 15 đến 100.",  exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBirthTooShort() {
        // Act & Assert
        CustomerAgeOutOfRangeException exception = assertThrows(
                CustomerAgeOutOfRangeException.class,
                () -> new DateOfBirth(LocalDate.of(2020, 1, 1))
        );
        // Optionally: Check context notice error
        assertEquals("Tuổi phải nằm trong khoảng từ 15 đến 100.",  exception.getMessage());
    }

}
