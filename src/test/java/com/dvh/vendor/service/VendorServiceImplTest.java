package com.dvh.vendor.service;

import com.dvh.common.exception.InvalidRoleException;
import com.dvh.common.exception.UserNotFoundException;
import com.dvh.user.constants.Role;
import com.dvh.user.entity.User;
import com.dvh.user.repository.UserRepository;
import com.dvh.vendor.exception.VendorAlreadyExistsException;
import com.dvh.vendor.mapper.VendorMapper;
import com.dvh.vendor.repository.VendorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
