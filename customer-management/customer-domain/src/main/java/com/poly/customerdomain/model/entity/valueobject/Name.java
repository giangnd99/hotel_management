package com.poly.customerdomain.model.entity.valueobject;

import com.poly.customerdomain.model.exception.BlankCustomerNameException;
import com.poly.customerdomain.model.exception.CustomerNameLengthOutOfRangeException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Name{

    private String firstName;
    private String lastName;

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 50;

    public Name(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank() &&  (lastName == null || lastName.isBlank())) {
            throw new BlankCustomerNameException();
        }
        if (firstName.trim().length() < MIN_NAME_LENGTH ||  firstName.trim().length() > MAX_NAME_LENGTH && lastName.trim().length() < MIN_NAME_LENGTH ||  lastName.trim().length() > MAX_NAME_LENGTH) {
            throw new CustomerNameLengthOutOfRangeException(MIN_NAME_LENGTH, MAX_NAME_LENGTH);
        }
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Name from(String firstName, String lastName) {
        return new Name(firstName, lastName);
    }

    public static String getFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }

    public static Name empty() {
        return new Name("Unnamed", "Unnamed");
    }

}