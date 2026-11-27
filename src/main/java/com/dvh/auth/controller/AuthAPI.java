package com.dvh.auth.controller;

import com.dvh.auth.dto.LoginRequest;
import com.dvh.auth.dto.LoginResponse;
import com.dvh.common.APIResponse;
import com.dvh.common.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "Login and token APIs")      // groups these endpoints in Swagger UI (dev only)
public interface AuthAPI {                                      // only documentation lives here

    @Operation(summary = "Login with email and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, access token returned"),
            @ApiResponse(responseCode = "400", description = "Validation failed or malformed request body",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Account is not active",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<APIResponse<LoginResponse>> login(LoginRequest loginRequest);
}
