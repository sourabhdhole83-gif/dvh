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
    @Size(max = 30, message = "City cannot exceed 30 characters")
    private String city;

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][1-9A-Z]Z[0-9A-Z]$", message = "Invalid GST number")
    private String gstNumber;
}
