package com.dvh.vendor.dto.response;

import lombok.*;

@Data
@Builder
public class VendorResponse {
    private Long id;
    private  String businessName;
    private String businessAddress;
    private String city;
    private String gstNumber;
    private boolean verified;
}
