package com.dvh.user.dto.response;

import com.dvh.user.constants.Role;
import com.dvh.user.constants.UserStatus;
import java.time.LocalDateTime;

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
