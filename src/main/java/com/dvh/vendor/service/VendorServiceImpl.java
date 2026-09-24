package com.dvh.vendor.service;

import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.repository.VendorRepository;
import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor@NoArgsConstructor
@Service
public class VendorServiceImpl implements VendorService{

    private VendorRepository vendorRepository;
    @Override
    public void createVendor(VendorCreateRequest createRequest) {

    }
}
