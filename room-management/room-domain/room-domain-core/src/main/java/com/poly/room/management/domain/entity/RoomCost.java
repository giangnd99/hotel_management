package com.poly.room.management.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomCost {

    private String id;
    private Room room;
    private Cost cost;
}
