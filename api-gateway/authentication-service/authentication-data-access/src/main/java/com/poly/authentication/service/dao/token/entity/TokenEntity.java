package com.poly.authentication.service.dao.token.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class TokenEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    String id;

    Date expiryTime;
}
