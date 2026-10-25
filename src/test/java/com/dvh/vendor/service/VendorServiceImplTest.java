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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class VendorServiceImplTest {

    private static final String USER_EMAIL = "vendor@gmail.com";

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private VendorMapper vendorMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VendorServiceImpl vendorService;

    private User vendorUser;
    private VendorCreateRequest request;

    @BeforeEach
    void setUp() {

        vendorUser = User.builder()
                .id(1L)
                .email(USER_EMAIL)
                .role(Role.VENDOR)
                .build();

        request = VendorCreateRequest.builder()
                .businessName("Hello Car")
                .businessAddress("Bhawarkuan")
                .city("INDORE")
                .gstNumber("23AAAAA0000A1Z5")
                .build();
    }

    @Test
    void createVendor_shouldThrowUserNotFoundException_whenUserDoesNotExist() {

        // Given
        given(userRepository.findByEmail(USER_EMAIL))
                .willReturn(Optional.empty());

        // When + Then
        assertThatThrownBy(() ->
                vendorService.createVendor(request, USER_EMAIL)
        )
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

        then(userRepository)
                .should()
                .findByEmail(USER_EMAIL);

        then(vendorRepository)
                .shouldHaveNoInteractions();

        then(vendorMapper)
                .shouldHaveNoInteractions();
    }

    @ParameterizedTest
    @EnumSource(
            value = Role.class,
            mode = EnumSource.Mode.EXCLUDE,
            names = "VENDOR"
    )
    void createVendor_shouldThrowInvalidRoleException_whenUserIsNotAVendor(
            Role role) {

        // Given
        vendorUser.setRole(role);

        given(userRepository.findByEmail(USER_EMAIL))
                .willReturn(Optional.of(vendorUser));

        // When + Then
        assertThatThrownBy(() ->
                vendorService.createVendor(request, USER_EMAIL)
        )
                .isInstanceOf(InvalidRoleException.class)
                .hasMessage("User is not a vendor");

        then(userRepository)
                .should()
                .findByEmail(USER_EMAIL);

        then(vendorRepository)
                .shouldHaveNoInteractions();

        then(vendorMapper)
                .shouldHaveNoInteractions();
    }

    @Test
    void createVendor_shouldThrowVendorAlreadyExistsException_whenVendorAlreadyExists() {

        // Given
        given(userRepository.findByEmail(USER_EMAIL))
                .willReturn(Optional.of(vendorUser));

        given(vendorRepository.existsByUserId(1L))
                .willReturn(true);

        // When + Then
        assertThatThrownBy(() ->
                vendorService.createVendor(request, USER_EMAIL)
        )
                .isInstanceOf(VendorAlreadyExistsException.class)
                .hasMessage("Vendor profile already exists");

        then(userRepository)
                .should()
                .findByEmail(USER_EMAIL);

        then(vendorRepository)
                .should()
                .existsByUserId(1L);

        then(vendorRepository)
                .should(org.mockito.Mockito.never())
                .save(any());

        then(vendorMapper)
                .shouldHaveNoInteractions();
    }

    @Test
    void createVendor_shouldLinkUserSaveAndReturnResponse_whenValidVendorUser() {

        // Given
        Vendor vendor = new Vendor();

        Vendor savedVendor = new Vendor();
        savedVendor.setId(10L);

        VendorResponse expectedResponse = VendorResponse.builder()
                .id(10L)
                .businessName("Hello Car")
                .businessAddress("Bhawarkuan")
                .gstNumber("23AAAAA0000A1Z5")
                .verified(false)
                .build();

        given(userRepository.findByEmail(USER_EMAIL))
                .willReturn(Optional.of(vendorUser));

        given(vendorRepository.existsByUserId(1L))
                .willReturn(false);

        given(vendorMapper.toEntity(request))
                .willReturn(vendor);

        given(vendorRepository.save(vendor))
                .willReturn(savedVendor);

        given(vendorMapper.toVendorResponse(savedVendor))
                .willReturn(expectedResponse);

        // When
        VendorResponse actual =
                vendorService.createVendor(request, USER_EMAIL);

        // Then
        assertThat(actual)
                .isSameAs(expectedResponse);

        then(userRepository)
                .should()
                .findByEmail(USER_EMAIL);

        then(vendorRepository)
                .should()
                .existsByUserId(1L);

        then(vendorMapper)
                .should()
                .toEntity(request);

        then(vendorRepository)
                .should(times(1))
                .save(argThat(v ->
                        v != null && v.getUser() == vendorUser
                ));

        then(vendorMapper)
                .should()
                .toVendorResponse(savedVendor);
    }
}