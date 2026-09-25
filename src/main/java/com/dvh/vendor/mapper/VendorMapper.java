package com.dvh.vendor.mapper;

import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.dto.response.VendorResponse;
import com.dvh.vendor.entity.Vendor;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VendorMapper {

    @Mapping(target = "id", ignore = true)        // id remains null
    @Mapping(target = "user", ignore = true)      // user remains null
    @Mapping(target = "vehicles", ignore = true)  // vehicles gets its field initializer
    @Mapping(target = "verified", ignore = true)
    Vendor toEntity(VendorCreateRequest vendorCreateRequest);

    VendorResponse toVendorResponse(Vendor vendor);
}
