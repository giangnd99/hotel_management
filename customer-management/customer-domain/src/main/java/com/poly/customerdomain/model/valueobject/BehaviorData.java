package com.poly.customerdomain.model.valueobject;

import java.util.List;

public class BehaviorData {

    private final List<String> favoriteRoomTypes;   // Ví dụ: ["DELUXE", "SUITE"]
    private final List<String> frequentlyUsedServices; // Ví dụ: ["SPA", "BREAKFAST"]

    public BehaviorData(List<String> favoriteRoomTypes, List<String> frequentlyUsedServices) {
        this.favoriteRoomTypes = favoriteRoomTypes == null ? List.of() : favoriteRoomTypes;
        this.frequentlyUsedServices = frequentlyUsedServices == null ? List.of() : frequentlyUsedServices;
    }

    public List<String> getFavoriteRoomTypes() {
        return favoriteRoomTypes;
    }

    public List<String> getFrequentlyUsedServices() {
        return frequentlyUsedServices;
    }

    public boolean likesRoomType(String type) {
        return favoriteRoomTypes.contains(type);
    }

    public boolean usesService(String service) {
        return frequentlyUsedServices.contains(service);
    }

}
