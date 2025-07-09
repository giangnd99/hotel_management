package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.ERoomStatus;
import com.poly.room.management.domain.valueobject.RoomStatusId;

public class RoomStatus extends BaseEntity<RoomStatusId> {

    private ERoomStatus roomStatus;

    public RoomStatus() {
    }

    public RoomStatus(ERoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    private RoomStatus(Builder builder) {
        super.setId(builder.id);
        roomStatus = builder.roomStatus;
    }

    public String getRoomStatus() {
        return roomStatus.name();
    }

    public static final class Builder {
        private RoomStatusId id;
        private ERoomStatus roomStatus;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(RoomStatusId val) {
            id = val;
            return this;
        }

        public Builder roomStatus(ERoomStatus val) {
            roomStatus = val;
            return this;
        }

        public RoomStatus build() {
            return new RoomStatus(this);
        }
    }
}
