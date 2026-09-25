package com.dvh.vendor.service;

import com.dvh.user.entity.User;
import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.dto.response.VendorResponse;
import jakarta.validation.Valid;

public interface VendorService {

    VendorResponse createVendor(@Valid VendorCreateRequest createRequest, User user);
}
