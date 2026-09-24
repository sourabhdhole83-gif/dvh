package com.dvh.vendor.service;

import com.dvh.vendor.dto.request.VendorCreateRequest;
import jakarta.validation.Valid;

public interface VendorService {

    void createVendor(@Valid VendorCreateRequest createRequest);
}
