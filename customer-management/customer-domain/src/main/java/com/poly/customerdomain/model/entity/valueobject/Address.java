package com.poly.customerdomain.model.entity.valueobject;

import com.poly.customerdomain.model.exception.CustomerAddressLengthOutOfRangeException;
import lombok.ToString;

public class Address {

    private String street;
    private String ward;
    private String district;
    private String city;

    public Address(String street, String ward, String district, String city) {
        if (isLenghtOutOfRange(street, ward, district, city)) {
            throw new CustomerAddressLengthOutOfRangeException(5, 100);
        }
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
    }

    private static boolean isLenghtOutOfRange(String street, String ward, String district, String city) {
        int min = 5;
        int max = 100;
        return street.trim().length() < min || ward.trim().length() < min || district.trim().length() < min || city.trim().length() < min
                || street.trim().length() > max || ward.trim().length() > max || district.trim().length() > max || city.trim().length() > max;
    }


    public static Address empty() {
        return new Address("Unnamed", "Unnamed", "Unnamed", "Unnamed");
    }

    public String toFullAddress() {
        return "%s, %s, %s, %s".formatted(street, ward, district, city);
    }

    public static Address from(String fullAddress) {
        if (fullAddress == null || fullAddress.isBlank()) {
            return Address.empty();
        }
        String[] parts = fullAddress.split(",\\s*");
        String street = parts.length > 0 ? parts[0] : "";
        String ward = parts.length > 1 ? parts[1] : "";
        String district = parts.length > 2 ? parts[2] : "";
        String city = parts.length > 3 ? parts[3] : "";

        return new Address(street, ward, district, city);
    }

    public static Address from(String street, String ward, String district, String city) {
        return new Address(street, ward, district, city);
    }

}