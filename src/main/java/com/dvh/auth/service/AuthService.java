package com.dvh.auth.service;

import com.dvh.auth.dto.LoginRequest;
import com.dvh.auth.dto.LoginResponse;


public interface AuthService {
     LoginResponse login(LoginRequest loginRequest);
}
