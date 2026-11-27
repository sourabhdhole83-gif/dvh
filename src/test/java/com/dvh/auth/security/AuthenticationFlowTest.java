package com.dvh.auth.security;

import com.dvh.auth.config.SecurityConfig;
import com.dvh.auth.config.PasswordConfig;
import com.dvh.user.constants.Role;
import com.dvh.user.constants.UserStatus;
import com.dvh.user.entity.User;
import com.dvh.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringJUnitConfig(AuthenticationFlowTest.TestConfig.class)   // starts a tiny Spring context with only the beans below
class AuthenticationFlowTest {

    @Configuration
    @EnableGlobalAuthentication                                // switches on the machinery that builds the AuthenticationManager
    @Import({PasswordConfig.class, CustomUserDetailsService.class})   // the same real beans the app uses
    static class TestConfig {
        @Bean
        UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);         // only the database is fake
        }
    }

    @Autowired private AuthenticationConfiguration authenticationConfiguration;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;        // the real BCrypt bean

    private AuthenticationManager manager() throws Exception {
        return new SecurityConfig().authenticationManager(authenticationConfiguration);   // our real bean method
    }

    private User userWithPassword(String rawPassword) {
        return User.builder().id(1L).email("test@mail.com")
                .passwordHash(passwordEncoder.encode(rawPassword))   // hashed exactly like registration does it
                .role(Role.VENDOR).userStatus(UserStatus.ACTIVE).build();
    }

    @Test
    void authenticate_shouldSucceedWithRoleAuthority_whenPasswordMatches() throws Exception {
        given(userRepository.findByEmail("test@mail.com")).willReturn(Optional.of(userWithPassword("Secret@123")));

        Authentication result = manager().authenticate(
                new UsernamePasswordAuthenticationToken("test@mail.com", "Secret@123"));

        assertThat(result.isAuthenticated()).isTrue();                          // the flag flipped to true
        assertThat(result.getPrincipal()).isInstanceOf(CustomUserDetails.class); // principal is now our adapter (the getPrincipal line!)
        assertThat(result.getAuthorities()).extracting(Object::toString).contains("ROLE_VENDOR"); // contains = ROLE_VENDOR must be present; extra Spring-added authorities like FACTOR_PASSWORD are allowed
    }

    @Test
    void authenticate_shouldThrowBadCredentials_whenPasswordIsWrong() throws Exception {
        given(userRepository.findByEmail("test@mail.com")).willReturn(Optional.of(userWithPassword("Secret@123")));

        assertThatThrownBy(() -> manager().authenticate(
                new UsernamePasswordAuthenticationToken("test@mail.com", "Wrong@123")))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void authenticate_shouldThrowBadCredentials_whenEmailDoesNotExist() throws Exception {
        given(userRepository.findByEmail("nobody@mail.com")).willReturn(Optional.empty());

        assertThatThrownBy(() -> manager().authenticate(
                new UsernamePasswordAuthenticationToken("nobody@mail.com", "Secret@123")))
                .isInstanceOf(BadCredentialsException.class);   // same exception as a wrong password, so no user enumeration
    }
}