package com.dvh.vendor.controller;

import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.service.VendorService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<String> createVendor(@Valid @RequestBody VendorCreateRequest createRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        log.info("Logged-in user: {}", userName );

        vendorService.createVendor(createRequest);
        return ResponseEntity.ok("Vendor created successfully!!");
    }
}
