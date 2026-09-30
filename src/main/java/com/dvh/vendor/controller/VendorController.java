package com.dvh.vendor.controller;

import com.dvh.common.APIResponse;
import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.dto.response.VendorResponse;
import com.dvh.vendor.service.VendorService;
import jakarta.validation.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vendor")
public class VendorController {

    private final VendorService vendorService;

    @PostMapping("/createVendor")
    public ResponseEntity<APIResponse<VendorResponse>> createVendor(@Valid @RequestBody VendorCreateRequest createRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        log.info("Logged-in user: {}", userName );

        VendorResponse response = vendorService.createVendor(createRequest, userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new APIResponse<>(true,"Vendor created successfully",response));
    }
}
