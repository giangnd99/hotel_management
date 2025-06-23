package com.poly.authentication.service.dao.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poly.authentication.service.dao.role.entity.RoleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Pattern(regexp = "0\\d{9,10}", message = " Invalid phone format. Phone must start with 0 and contain 10-11 digits")
    private String phone;

    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private RoleEntity role;

}