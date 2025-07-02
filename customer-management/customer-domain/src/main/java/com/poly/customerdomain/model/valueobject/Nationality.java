package com.poly.customerdomain.model.valueobject;

import com.poly.customerdomain.model.exception.CustomerNationalityOutOfRangeException;

public class Nationality{

    private String nationality;

    public Nationality (String nationality){
        if(nationality == null && nationality.isEmpty()){
            this.nationality = "none";
        }
        if (nationality.trim().length() < 2 || nationality.trim().length() > 20){throw new CustomerNationalityOutOfRangeException(2, 20);}
        this.nationality = nationality;
    }
}
