package com.dvh.user.controller;

import com.dvh.common.APIResponse;
import com.dvh.user.dto.request.UserCreateRequest;
import com.dvh.user.dto.response.UserResponse;
import com.dvh.user.service.UserService;
import jakarta.validation.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;


    @PostMapping
    public ResponseEntity<APIResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest){
        UserResponse user = userService.createUser(userCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(true,"User created successfully",user));

    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponse>> getUserById(@PathVariable Long id){
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(true, "User details fetched successfully", userResponse));    }
}
