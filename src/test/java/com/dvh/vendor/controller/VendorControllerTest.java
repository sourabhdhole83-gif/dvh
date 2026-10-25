package com.dvh.vendor.controller;

import com.dvh.common.APIResponse;
import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.dto.response.VendorResponse;
import com.dvh.vendor.service.VendorService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class VendorControllerTest {

    @Mock
    private VendorService vendorService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private VendorController vendorController;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createVendor_shouldReturnCreatedResponse_whenRequestIsValid() {

        // Given
        String email = "vendor@gmail.com";

        VendorCreateRequest request = VendorCreateRequest.builder()
                .businessName("Hello Car")
                .businessAddress("Bhawarkuan")
                .city("INDORE")
                .gstNumber("23AAAAA0000A1Z5")
                .build();

        VendorResponse vendorResponse = VendorResponse.builder()
                .id(2L)
                .businessName("Hello Car")
                .businessAddress("Bhawarkuan")
                .city("INDORE")
                .gstNumber("23AAAAA0000A1Z5")
                .verified(false)
                .build();

        //Set the fake authentication directly into the real SecurityContext
        // Standard Mockito cannot mock static methods, so trying to use when() on SecurityContextHolder.getContext() throws a NullPointerException before Mockito can even intercept it.
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        given(authentication.getName())
                .willReturn(email);

        given(vendorService.createVendor(request, email))
                .willReturn(vendorResponse);

        // When
        ResponseEntity<APIResponse<VendorResponse>> response =
                vendorController.createVendor(request);

        // Then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody())
                .isNotNull();

        assertThat(response.getBody().getData())
                .isSameAs(vendorResponse);

        then(vendorService)
                .should()
                .createVendor(request, email);
    }
}