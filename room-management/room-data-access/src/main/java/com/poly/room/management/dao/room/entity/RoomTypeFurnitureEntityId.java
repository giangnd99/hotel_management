package com.poly.room.management.dao.room.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomTypeFurnitureEntityId {

    private RoomTypeEntity roomType;

    private FurnitureEntity furniture;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomTypeFurnitureEntityId that = (RoomTypeFurnitureEntityId) o;
        return Objects.equals(roomType, that.roomType) && Objects.equals(furniture, that.furniture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomType, furniture);
    }
}
