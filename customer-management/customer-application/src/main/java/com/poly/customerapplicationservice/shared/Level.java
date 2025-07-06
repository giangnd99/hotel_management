package com.poly.customerapplicationservice.shared;

public enum Level {
    NONE, BRONZE, SILVER, GOLD;

    public static Level fromDomain(com.poly.customerdomain.model.entity.valueobject.Level domainLevel) {
        return Level.valueOf(domainLevel.name());
    }
}
