package com.poly.customerapplicationservice.shared;

public enum Level {
    NONE, BRONZE, SILVER, GOLD, PLATINUM, DIAMOND;

    public static Level fromDomain(com.poly.customerdomain.model.entity.valueobject.Level domainLevel) {
        return Level.valueOf(domainLevel.name());
    }
}
