package com.poly.customerdomain.model.entity.valueobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BehaviorData {

    private String favoriteRoomTypes;
    private String frequentlyUsedServices;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public BehaviorData() {

    }

    public BehaviorData(String favoriteRoomTypes, String frequentlyUsedServices) {
        this.favoriteRoomTypes = favoriteRoomTypes == null ? "Unnamed" : favoriteRoomTypes;
        this.frequentlyUsedServices = frequentlyUsedServices == null ? "Unnamed": frequentlyUsedServices;
    }

    // ✅ Serialize
    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot serialize BehaviorData to JSON", e);
        }
    }

    // ✅ Deserialize
    public static BehaviorData fromJson(String json) {
        try {
            return objectMapper.readValue(json, BehaviorData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot deserialize BehaviorData from JSON", e);
        }
    }

    public static BehaviorData empty() {
        return new BehaviorData("Unnamed", "Unnamed");
    }

    public String getFavoriteRoomTypes() {
        return favoriteRoomTypes;
    }

    public String getFrequentlyUsedServices() {
        return frequentlyUsedServices;
    }

    public boolean likesRoomType(String type) {
        return favoriteRoomTypes.contains(type);
    }

    public boolean usesService(String service) {
        return frequentlyUsedServices.contains(service);
    }

}
