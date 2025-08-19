package com.poly.room.management.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoomId {
    private UUID value;
    
    public static RoomId of(UUID value) {
        return new RoomId(value);
    }
    
    public static RoomId generate() {
        return new RoomId(UUID.randomUUID());
    }
}
