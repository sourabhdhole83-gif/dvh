package com.dvh.vendor.controller;

import com.dvh.common.APIResponse;
import com.dvh.user.entity.User;
import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.dto.response.VendorResponse;
import com.dvh.vendor.service.VendorService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor@NoArgsConstructor
@RestController
@RequestMapping("/api/v1/vendor")
public class VendorController {

    private VendorService vendorService;

    @PostMapping("/createVendor")
    public ResponseEntity<APIResponse<VendorResponse>> createVendor(@Valid @RequestBody VendorCreateRequest createRequest, User user){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        log.info("Logged-in user: {}", userName );

        VendorResponse response = vendorService.createVendor(createRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new APIResponse<>(true,"Vendor created successfully",response));
    }
}
