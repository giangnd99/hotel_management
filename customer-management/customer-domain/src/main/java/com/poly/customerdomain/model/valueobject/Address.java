package com.poly.customerdomain.model.valueobject;

import com.poly.customerdomain.model.exception.CustomerAddressLengthOutOfRangeException;

public class Address{

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

    public String fullAddress() {
        return "%s, %s, %s, %s".formatted(street, ward, district, city);
    }
}