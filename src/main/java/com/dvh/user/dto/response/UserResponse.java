package com.dvh.user.dto.response;

import com.dvh.user.constants.Role;
import com.dvh.user.constants.UserStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.LocalDateTime;
@Data
@JsonPropertyOrder({
        "id",
        "name",
        "email",
        "phone",
        "role",
        "userStatus",
        "createdAt",
        "updatedAt"
})
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private UserStatus userStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
