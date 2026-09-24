package com.dvh.vendor.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;


@Data@AllArgsConstructor@NoArgsConstructor
public class VendorCreateRequest {

    @NotBlank(message = "Business name is required")
    @Size(min = 3,max = 100,message = "Business name must be 3 to 100 characters" )
    private String businessName;

    @NotBlank(message = "Business address is required")
    @Size(max = 255, message = "Business address cannot exceed 255 characters")
    private String businessAddress;

    @NotBlank(message = "Business city is required")
    private String city;

    @NotBlank(message = "GST number is required")
    @Size(min = 15, max = 15, message = "GST number should have 15 characters")
    private String gstNumber;
}
