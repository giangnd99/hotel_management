package com.poly.customerdomain.model.entity.valueobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class BehaviorData {

    private final List<String> favoriteRoomTypes;   // Ví dụ: ["DELUXE", "SUITE"]
    private final List<String> frequentlyUsedServices; // Ví dụ: ["SPA", "BREAKFAST"]

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public BehaviorData() {
        this.favoriteRoomTypes = List.of();
        this.frequentlyUsedServices = List.of();
    }


    public BehaviorData(List<String> favoriteRoomTypes, List<String> frequentlyUsedServices) {
        this.favoriteRoomTypes = favoriteRoomTypes == null ? List.of() : favoriteRoomTypes;
        this.frequentlyUsedServices = frequentlyUsedServices == null ? List.of() : frequentlyUsedServices;
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
        return new BehaviorData(List.of(), List.of());
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
