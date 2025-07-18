package com.poly.customerdomain.model.entity.valueobject;

public class Description {
    private String description;

    public Description(String description) {
        this.description = description;
    }

    public static Description from(String description) {
        return new Description(description);
    }
}
