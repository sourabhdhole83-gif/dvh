package com.dvh.vendor.service;

import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.dto.response.VendorResponse;
import jakarta.validation.Valid;

public interface VendorService {

    VendorResponse createVendor(@Valid VendorCreateRequest createRequest, String userEmail);
}
