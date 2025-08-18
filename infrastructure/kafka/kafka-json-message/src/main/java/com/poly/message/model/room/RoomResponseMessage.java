package com.poly.message.model.room;

import com.poly.message.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder

public class RoomResponseMessage extends BaseResponse {

    private String roomId;
    private String roomApprovalStatus;
    private String reason;
    private Long processedAt;

    public RoomResponseMessage() {
        setMessageType(com.poly.message.MessageType.RESPONSE);
    }
}
