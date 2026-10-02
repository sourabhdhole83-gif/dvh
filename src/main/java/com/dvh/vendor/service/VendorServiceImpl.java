package com.dvh.vendor.service;

import com.dvh.common.exception.InvalidRoleException;
import com.dvh.common.exception.UserNotFoundException;
import com.dvh.user.repository.UserRepository;
import com.dvh.vendor.exception.VendorAlreadyExistsException;
import com.dvh.user.constants.Role;
import com.dvh.user.entity.User;
import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.dto.response.VendorResponse;
import com.dvh.vendor.entity.Vendor;
import com.dvh.vendor.mapper.VendorMapper;
import com.dvh.vendor.repository.VendorRepository;
import lombok.*;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class VendorServiceImpl implements VendorService{

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;
    private final UserRepository userRepository;

    @Override
    public VendorResponse createVendor(VendorCreateRequest createRequest, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if(user.getRole()!= Role.VENDOR){
            throw new InvalidRoleException("User is not a vendor");
        }

        if(vendorRepository.existsByUserId(user.getId())){
            throw new VendorAlreadyExistsException("Vendor profile already exists");
        }

        Vendor vendor = vendorMapper.toEntity(createRequest);
        vendor.setUser(user);
        Vendor savedVendor = vendorRepository.save(vendor);

        VendorResponse vendorResponse = vendorMapper.toVendorResponse(savedVendor);

        return vendorResponse;


    }
}
