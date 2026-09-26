package com.dvh.user.service;

import com.dvh.user.dto.request.UserCreateRequest;
import com.dvh.user.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(UserCreateRequest userCreateRequest);

    UserResponse getUserById(Long id);
}
