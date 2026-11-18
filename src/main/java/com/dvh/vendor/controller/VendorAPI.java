package com.dvh.vendor.controller;

import com.dvh.common.APIResponse;
import com.dvh.common.ErrorResponse;
import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.dto.response.VendorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Vendor", description = "Vendor profile APIs")   // Swagger mein "Vendor" naam ka group
public interface VendorAPI {

    @Operation(summary = "Create vendor profile for the logged-in user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vendor created successfully"),

            // 400: @Valid fail (VALIDATION_ERROR) ya JSON toota (MALFORMED_REQUEST)
            @ApiResponse(responseCode = "400", description = "Validation failed or malformed request body",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

            // 403: InvalidRoleException (user ka role vendor banne ke liye allowed nahi)
            @ApiResponse(responseCode = "403", description = "User role is not allowed to create a vendor profile",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

            // 404: UserNotFoundException (email se user nahi mila)
            @ApiResponse(responseCode = "404", description = "Logged-in user not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

            // 409: VendorAlreadyExistsException ya GST number duplicate (DataIntegrityViolation)
            @ApiResponse(responseCode = "409", description = "Vendor profile already exists or GST number is duplicate",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<APIResponse<VendorResponse>> createVendor(VendorCreateRequest createRequest);  // sirf signature
}