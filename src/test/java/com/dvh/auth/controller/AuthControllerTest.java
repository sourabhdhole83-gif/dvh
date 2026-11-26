package com.dvh.auth.controller;

import com.dvh.auth.service.AuthService;
import com.dvh.auth.dto.LoginRequest;
import com.dvh.auth.dto.LoginResponse;
import com.dvh.auth.exception.AccountNotActiveException;
import com.dvh.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)                     // lets Mockito create the @Mock objects for each test
class AuthControllerTest {

    @Mock
    private AuthService authService;                    // fake service: this test only checks HTTP behaviour, not login logic

    @InjectMocks
    private AuthController authController;              // the real controller, the fake service is injected into it

    private MockMvc mockMvc;                            // a pretend browser that sends requests without starting the server

    @BeforeEach
    void setUp() {                                      // runs before every test
        mockMvc = MockMvcBuilders.standaloneSetup(authController)   // real Spring MVC (JSON, @Valid) with no MySQL and no full app
                .setControllerAdvice(new GlobalExceptionHandler())  // so errors go through our real handler
                .build();
    }

    private static final String VALID_BODY = "{\"email\":\"test@mail.com\",\"password\":\"Secret@123\"}";  // reusable JSON body

    @Test
    void login_shouldReturn200WithToken_whenCredentialsAreValid() throws Exception {
        given(authService.login(any(LoginRequest.class)))           // Given: the fake service succeeds
                .willReturn(new LoginResponse("jwt-token", "Bearer", 900));

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY)) // When: POST arrives
                .andExpect(status().isOk())                                  // Then: HTTP 200
                .andExpect(jsonPath("$.success").value(true))                // wrapper says success
                .andExpect(jsonPath("$.data.accessToken").value("jwt-token"))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.expiresIn").value(900));
    }

    @Test
    void login_shouldReturn400_whenEmailIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\",\"password\":\"Secret@123\"}"))   // blank email breaks @NotBlank
                .andExpect(status().isBadRequest())                          // 400, and the service is never called
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void login_shouldReturn401WithGenericMessage_whenCredentialsAreInvalid() throws Exception {
        given(authService.login(any(LoginRequest.class)))
                .willThrow(new BadCredentialsException("details that must not leak"));   // service says bad credentials

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isUnauthorized())                        // 401
                .andExpect(jsonPath("$.errorCode").value("INVALID_CREDENTIALS"))
                .andExpect(jsonPath("$.message").value("Invalid email or password")); // fixed text, the leaky message never reaches the client
    }

    @Test
    void login_shouldReturn403_whenAccountIsNotActive() throws Exception {
        given(authService.login(any(LoginRequest.class)))
                .willThrow(new AccountNotActiveException("Account is not active"));

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isForbidden())                           // 403
                .andExpect(jsonPath("$.errorCode").value("ACCOUNT_NOT_ACTIVE"));
    }
}