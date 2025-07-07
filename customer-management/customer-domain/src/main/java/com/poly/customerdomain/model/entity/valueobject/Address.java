package com.poly.customerdomain.model.entity.valueobject;

import com.poly.customerdomain.model.exception.CustomerAddressLengthOutOfRangeException;

public class Address {

    private String street;
    private String ward;
    private String district;
    private String city;

    private static final int MIN_ADDRESS_LENGTH = 3;
    private static final int MAX_ADDRESS_LENGTH = 100;

    public Address(String street, String ward, String district, String city) {
        if (isLenghtOutOfRange(street, ward, district, city)) {
            throw new CustomerAddressLengthOutOfRangeException(MIN_ADDRESS_LENGTH, MAX_ADDRESS_LENGTH);
        }
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
    }

    private static boolean isLenghtOutOfRange(String street, String ward, String district, String city) {
        return street.trim().length() < MIN_ADDRESS_LENGTH || ward.trim().length() < MIN_ADDRESS_LENGTH || district.trim().length() < MIN_ADDRESS_LENGTH || city.trim().length() < MIN_ADDRESS_LENGTH
                || street.trim().length() > MAX_ADDRESS_LENGTH || ward.trim().length() > MAX_ADDRESS_LENGTH || district.trim().length() > MAX_ADDRESS_LENGTH || city.trim().length() > MAX_ADDRESS_LENGTH;
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