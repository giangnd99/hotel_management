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
public class GuestId {
    private UUID value;
    
    public static GuestId of(UUID value) {
        return new GuestId(value);
    }
    
    public static GuestId generate() {
        return new GuestId(UUID.randomUUID());
    }
}
