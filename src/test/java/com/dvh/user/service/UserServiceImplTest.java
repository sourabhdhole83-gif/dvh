package com.dvh.user.service;

import com.dvh.common.exception.*;
import com.dvh.user.constants.Role;
import com.dvh.user.constants.UserStatus;
import com.dvh.user.dto.request.UserCreateRequest;
import com.dvh.user.dto.response.UserResponse;
import com.dvh.user.entity.User;
import com.dvh.user.exception.UserAlreadyExistsException;
import com.dvh.user.mapper.UserMapper;
import com.dvh.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper mapper;
    @Mock private PasswordEncoder encoder;

    @InjectMocks private UserServiceImpl userService;

    private UserCreateRequest request;

    @BeforeEach
    void setUp() {
        request = new UserCreateRequest(
                "Sourabh", "test@gmail.com", "9876543210", "Test@1234", Role.CUSTOMER);
    }

    @Test
    void createUser_shouldSaveUserWithHashedPasswordAndActiveStatus() {
        User mapped = User.builder()
                .name("Sourabh").email("test@gmail.com").phone("9876543210")
                .role(Role.CUSTOMER).build();
        UserResponse response = new UserResponse();

        given(userRepository.existsByEmail("test@gmail.com")).willReturn(false);
        given(userRepository.existsByPhone("9876543210")).willReturn(false);
        given(mapper.toUserEntity(request)).willReturn(mapped);
        given(encoder.encode("Test@1234")).willReturn("hashed-password");
        given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));
        given(mapper.toUserResponse(any(User.class))).willReturn(response);

        UserResponse result = userService.createUser(request);

        assertThat(result).isSameAs(response);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        then(userRepository).should().save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getPasswordHash()).isEqualTo("hashed-password");
        assertThat(saved.getPasswordHash()).isNotEqualTo("Test@1234");
        assertThat(saved.getUserStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void createUser_shouldRejectAdminRole_withoutTouchingDatabase() {
        request.setRole(Role.ADMIN);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(InvalidRoleException.class);

        then(userRepository).shouldHaveNoInteractions();
    }

    @Test
    void createUser_shouldReportEmailError_whenEmailAlreadyExists() {
        given(userRepository.existsByEmail("test@gmail.com")).willReturn(true);
        given(userRepository.existsByPhone("9876543210")).willReturn(false);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOfSatisfying(UserAlreadyExistsException.class, ex ->
                        assertThat(ex.getErrors()).containsOnlyKeys("email"));

        then(userRepository).should(never()).save(any());
    }

    @Test
    void createUser_shouldReportBothErrors_whenEmailAndPhoneExist() {
        given(userRepository.existsByEmail("test@gmail.com")).willReturn(true);
        given(userRepository.existsByPhone("9876543210")).willReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOfSatisfying(UserAlreadyExistsException.class, ex ->
                        assertThat(ex.getErrors()).containsOnlyKeys("email", "phone"));
    }

    @Test
    void getUserById_shouldReturnUser_whenFound() {
        User user = User.builder().id(1L).build();
        UserResponse response = new UserResponse();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(mapper.toUserResponse(user)).willReturn(response);

        assertThat(userService.getUserById(1L)).isSameAs(response);
    }

    @Test
    void getUserById_shouldThrowUserNotFound_whenMissing() {
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with id: 99");
    }
}