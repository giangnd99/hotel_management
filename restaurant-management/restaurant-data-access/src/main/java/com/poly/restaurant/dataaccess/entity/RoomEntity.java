package com.poly.restaurant.dataaccess.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class RoomEntity {

    @Id
    private String id;
    private String roomNumber;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomOrderEntity> orders;

}
