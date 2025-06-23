package com.springboot.asm.fpoly_asm_springboot.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;

@RedisHash("forgotPasswordToken")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ForgotPasswordToken implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String token;
    private String email;
    private long expiresTime;

}
