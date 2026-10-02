package com.dvh.vendor.service;

import com.dvh.common.exception.InvalidRoleException;
import com.dvh.common.exception.UserNotFoundException;
import com.dvh.user.constants.Role;
import com.dvh.user.entity.User;
import com.dvh.user.repository.UserRepository;
import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.dto.response.VendorResponse;
import com.dvh.vendor.entity.Vendor;
import com.dvh.vendor.exception.VendorAlreadyExistsException;
import com.dvh.vendor.mapper.VendorMapper;
import com.dvh.vendor.repository.VendorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class) //Enable Mockito in JUnit 5
public class VendorServiceImplTest {

    @Mock //creates fake dependencies/creates a fake VendorRepository object
    private VendorRepository vendorRepository;

    @Mock
    private VendorMapper vendorMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks //creates the real class under test and injects those mocks into it
    private VendorServiceImpl vendorService;

    @Test
    void createVendor_shouldThrowUserNotFoundException_whenUserDoesNotExist(){

        String userEmail = "vendor@gmail.com";

        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                ()->vendorService.createVendor(null,userEmail));
    }
    @Test
    void createVendor_shouldThrowInvalidRoleException_whenUserIsNotVendor(){
        String userEmail = "customer@gmail.com";

        User user = new User();
        user.setEmail(userEmail);
        user.setId(1L);
        user.setRole(Role.CUSTOMER);

        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(user));

        assertThrows(InvalidRoleException.class,
                ()->vendorService.createVendor(null,userEmail));
    }

    @Test
    void creteVendor_shouldThrowVendorAlreadyExistsException_whenVendorAlreadyExists(){
        String userEmail = "vendor@gmail.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(userEmail);
        user.setRole(Role.VENDOR);

        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(user));

        when(vendorRepository.existsByUserId(user.getId()))
                .thenReturn(true);

        assertThrows(VendorAlreadyExistsException.class,
                ()->vendorService.createVendor(null,userEmail));
    }

    @Test
    void createVendor_shouldCreateVendor_whenValidVendorUser(){
        //ARRANGE
        String userEmail = "vendor@email.com";

        User user = new User();
        user.setId(1L);
        user.setRole(Role.VENDOR);
        user.setEmail(userEmail);

        VendorCreateRequest vendorCreateRequest = VendorCreateRequest.builder()
                        .businessName("Hello car")
                        .businessAddress("Bhawarkuan")
                        .city("INDORE")
                        .gstNumber("90238")
                        .build();

        VendorResponse expectedResponse = VendorResponse.builder()
                .id(2L)
                .businessName("Hello car")
                .businessAddress("Bhawarkuan")
                .city("INDORE")
                .gstNumber("90238")
                .verified(false)
                .build();

        Vendor vendor = new Vendor();
        Vendor savedVendor = new Vendor();

        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(user));

        when(vendorRepository.existsByUserId(user.getId()))
                .thenReturn(false);

        when(vendorMapper.toEntity(vendorCreateRequest))
                .thenReturn(vendor);

        when(vendorRepository.save(vendor))
                .thenReturn(savedVendor);

        when(vendorMapper.toVendorResponse(savedVendor))
                .thenReturn(expectedResponse);

        //ACT
        VendorResponse actualResponse = vendorService.createVendor(vendorCreateRequest, userEmail);

        //ASSERT
        assertNotNull(expectedResponse);
        assertEquals(expectedResponse,actualResponse);

        verify(userRepository).findByEmail(userEmail);
        verify(vendorRepository).existsByUserId(user.getId());
        verify(vendorMapper).toEntity(vendorCreateRequest);
        verify(vendorRepository).save(vendor);
        verify(vendorMapper).toVendorResponse(savedVendor);

    }
    
}
