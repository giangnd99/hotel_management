package com.poly.room.management.dao.room.entity;

import com.poly.domain.valueobject.RoomStatus;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID roomId;

    @OneToOne(mappedBy = "room",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RoomTypeEntity roomType;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoomMaintenanceEntity> roomMaintenances;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    private String roomNumber;

    private int floor;

    private String area;
}
