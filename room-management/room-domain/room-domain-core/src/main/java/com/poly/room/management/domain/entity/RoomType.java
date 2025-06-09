package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.room.management.domain.valueobject.RoomTypeId;

import java.util.List;

public class RoomType extends BaseEntity<RoomTypeId> {

    private String typeName;
    private String description;
    private List<Furniture> furnitures;


}
