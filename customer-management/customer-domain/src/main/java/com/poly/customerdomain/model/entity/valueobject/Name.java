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

    public Name(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank() &&  (lastName == null || lastName.isBlank())) {
            throw new BlankCustomerNameException();
        }
        if (firstName.trim().length() < 5 ||  firstName.trim().length() > 100 && lastName.trim().length() < 5 ||  lastName.trim().length() > 100) {
            throw new CustomerNameLengthOutOfRangeException(5, 100);
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