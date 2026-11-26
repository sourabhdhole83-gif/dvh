package com.dvh.auth.service;

import com.dvh.auth.dto.LoginRequest;
import com.dvh.auth.dto.LoginResponse;
import com.dvh.auth.exception.AccountNotActiveException;
import com.dvh.auth.security.CustomUserDetails;
import com.dvh.user.constants.Role;
import com.dvh.user.constants.UserStatus;
import com.dvh.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;    // fake login engine: we decide what it returns or throws

    @Mock
    private JwtService jwtService;                          // fake token maker, so only AuthServiceImpl's own logic is tested

    @InjectMocks
    private AuthServiceImpl authService;                    // the real class under test, both mocks injected

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()                               // a ready-made active customer used by most tests
                .id(1L)
                .email("test@mail.com")
                .passwordHash("hash")
                .role(Role.CUSTOMER)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }

    private Authentication authenticated(User u) {          // what the real AuthenticationManager returns after a successful login
        CustomUserDetails principal = new CustomUserDetails(u);
        return UsernamePasswordAuthenticationToken.authenticated(principal, null, principal.getAuthorities());
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        given(authenticationManager.authenticate(any())).willReturn(authenticated(user));  // Given: login engine says OK
        given(jwtService.generateAccessToken(user)).willReturn("jwt-token");
        given(jwtService.getAccessTokenExpirySeconds()).willReturn(900L);

        LoginResponse response = authService.login(new LoginRequest("test@mail.com", "Secret@123")); // When

        assertThat(response.accessToken()).isEqualTo("jwt-token");    // Then: record accessors, no "get" prefix
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresIn()).isEqualTo(900L);
    }

    @Test
    void login_shouldTrimAndLowercaseEmail_beforeAuthenticating() {
        given(authenticationManager.authenticate(any())).willReturn(authenticated(user));
        given(jwtService.generateAccessToken(user)).willReturn("jwt-token");

        authService.login(new LoginRequest("  Test@Mail.COM ", "Secret@123"));   // messy email typed by the user

        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class); // catches what we passed to the manager
        verify(authenticationManager).authenticate(captor.capture());
        assertThat(captor.getValue().getPrincipal()).isEqualTo("test@mail.com"); // the manager must receive the clean email
    }

    @Test
    void login_shouldPropagateBadCredentials_andNotIssueToken_whenPasswordIsWrong() {
        given(authenticationManager.authenticate(any())).willThrow(new BadCredentialsException("bad")); // engine rejects

        assertThatThrownBy(() -> authService.login(new LoginRequest("test@mail.com", "wrong")))
                .isInstanceOf(BadCredentialsException.class);        // exception passes through to the handler
        verify(jwtService, never()).generateAccessToken(any());      // and no token was ever created
    }

    @Test
    void login_shouldThrowAccountNotActive_andNotIssueToken_whenUserIsBlocked() {
        user.setUserStatus(UserStatus.BLOCKED);                      // correct password but blocked account
        given(authenticationManager.authenticate(any())).willReturn(authenticated(user));

        assertThatThrownBy(() -> authService.login(new LoginRequest("test@mail.com", "Secret@123")))
                .isInstanceOf(AccountNotActiveException.class);
        verify(jwtService, never()).generateAccessToken(any());      // no token for a blocked user
    }
}