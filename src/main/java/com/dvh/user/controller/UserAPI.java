package com.dvh.user.controller;

import com.dvh.common.APIResponse;
import com.dvh.common.ErrorResponse;
import com.dvh.user.dto.request.UserCreateRequest;
import com.dvh.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User",description = "User creation and lookup APIs")
public interface UserAPI {

    @Operation(summary = "Create a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or malformed request body",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email or phone already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<APIResponse<UserResponse>> createUser(UserCreateRequest request);  // sirf signature, body nahi

    @Operation(summary = "Get a user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User details fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid id format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing, invalid or expired token",   // ek hi 401 entry, andar teen examples
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "No token", value = """
                                        {"timestamp":"2026-11-27T13:55:00","errorCode":"UNAUTHORIZED","message":"Authentication required","path":"/api/v1/users/1","details":null}"""),
                                    @ExampleObject(name = "Expired token", value = """
                                        {"timestamp":"2026-11-27T13:55:00","errorCode":"TOKEN_EXPIRED","message":"Token has expired, please login again","path":"/api/v1/users/1","details":null}"""),
                                    @ExampleObject(name = "Tampered token", value = """
                                        {"timestamp":"2026-11-27T13:55:00","errorCode":"TOKEN_INVALID","message":"Invalid token","path":"/api/v1/users/1","details":null}""")
                            })),
            @ApiResponse(responseCode = "403", description = "Logged in but not ADMIN",           // customer ne call kiya to ye
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Not an admin", value = """
                                {"timestamp":"2026-11-27T13:55:00","errorCode":"ACCESS_DENIED","message":"You do not have permission to access this resource","path":"/api/v1/users/1","details":null}"""))),

            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<APIResponse<UserResponse>> getUserById(Long id);
}
