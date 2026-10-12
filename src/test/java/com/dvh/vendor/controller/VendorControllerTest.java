package com.dvh.vendor.controller;

import com.dvh.common.APIResponse;
import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.dto.response.VendorResponse;
import com.dvh.vendor.service.VendorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendorControllerTest {
    @Mock
    private VendorService vendorService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private VendorController vendorController;

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }

    @Test
    void createVendor_shouldReturnVendorResponse_whenRequestIsValid(){

        //ARRANGE
        String email = "vendor@gmail.com";

        VendorCreateRequest vendorCreateRequest = VendorCreateRequest.builder()
                .businessName("Hello car")
                .businessAddress("Bhawarkuan")
                .city("INDORE")
                .gstNumber("1234")
                .build();

        VendorResponse vendorResponse = VendorResponse.builder()
                .id(2L)
                .businessName("Hello car")
                .businessAddress("Bhawarkuan")
                .city("INDORE")
                .gstNumber("1234")
                .verified(false)
                .build();

        //Set the fake authentication directly into the real SecurityContext
        // Standard Mockito cannot mock static methods, so trying to use when() on SecurityContextHolder.getContext() throws a NullPointerException before Mockito can even intercept it.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.getName())
                .thenReturn(email);

        when(vendorService.createVendor(vendorCreateRequest,email))
                .thenReturn(vendorResponse);

        //ACT
        ResponseEntity<APIResponse<VendorResponse>> response = vendorController.createVendor(vendorCreateRequest);

        //ASSERT
       assertNotNull(response.getBody());
       assertEquals(HttpStatus.CREATED,response.getStatusCode());

       verify(vendorService).createVendor(vendorCreateRequest,email);

    }

}
