package com.dvh.vendor.service;

import com.dvh.common.exception.InvalidRoleException;
import com.dvh.common.exception.UserNotFoundException;
import com.dvh.user.constants.Role;
import com.dvh.user.entity.User;
import com.dvh.user.repository.UserRepository;
import com.dvh.vendor.dto.request.VendorCreateRequest;
import com.dvh.vendor.mapper.VendorMapper;
import com.dvh.vendor.repository.VendorRepository;
import jakarta.persistence.Enumerated;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) //integrates Mockito with JUnit 5 and automatically initializes Mockito annotations such as @Mock and @InjectMocks
public class VendorServiceImplTest {

    private static final String userEmail = "Vendor@gmail.com";

    @Mock //creates fake dependencies/creates a fake VendorRepository object
    private VendorRepository vendorRepository;

    @Mock
    private VendorMapper vendorMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks //creates the real class under test and injects those mocks into it
    private VendorServiceImpl vendorService;

    private User vendorUser;
    private VendorCreateRequest request;

    @BeforeEach
    void setUp() {
         vendorUser = User.builder()
                .id(1L)
                .email(userEmail)
                .role(Role.VENDOR)
                .build();

         request = VendorCreateRequest.builder()
                .businessName("Hello Car")
                .businessAddress("Bhawarkuan")
                .gstNumber("23AAAAA0000A1Z5")
                .city("INDORE")
                .build();
    }

    @Test
    void createVendor_shouldThrowUserNotFoundException_whenUserDoesNotExist() {

        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> vendorService.createVendor(request, userEmail))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).existsByEmail(userEmail);
        verifyNoInteractions(vendorMapper, vendorRepository);
    }


    @ParameterizedTest
    @EnumSource(
            value = Role.class,
            mode = EnumSource.Mode.EXCLUDE,//A parameterized test that runs the validation logic(InvalidRole) for every role except 'VENDOR',
            names = "VENDOR"               // ensuring an exception is thrown for all non-vendor roles.

    )
    void createVendor_shouldThrowInvalidRoleException_whenUserIsNotAVendor(Role role){//except vendor all roles will come here

        vendorUser.setRole(role);

        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(vendorUser));

        assertThatThrownBy(()->vendorService.createVendor(request,userEmail))
                .isInstanceOf(InvalidRoleException.class)
                .hasMessage("User is not a vendor");

        verify(userRepository).findByEmail(userEmail);
        verifyNoInteractions(vendorRepository,vendorMapper);
    }


}
