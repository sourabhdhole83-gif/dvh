package com.dvh.auth.controller;

import com.dvh.auth.dto.LoginRequest;
import com.dvh.auth.dto.LoginResponse;
import com.dvh.auth.service.AuthService;
import com.dvh.common.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthAPI {

    public final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(true,"Login Successful",response));
    }
}
