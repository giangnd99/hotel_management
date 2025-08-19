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
public class CheckInId {
    private UUID value;
    
    public static CheckInId of(UUID value) {
        return new CheckInId(value);
    }
    
    public static CheckInId generate() {
        return new CheckInId(UUID.randomUUID());
    }
}
