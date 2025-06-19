package com.poly.ai.management.dao.redis.model;

import com.poly.ai.management.domain.dto.RedisMessageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@RedisHash("chat_session")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisChatSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private List<RedisMessageDto> messages;


}
