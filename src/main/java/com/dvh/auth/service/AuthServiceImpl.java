package com.dvh.auth.service;

import com.dvh.auth.dto.LoginRequest;
import com.dvh.auth.dto.LoginResponse;
import com.dvh.auth.exception.AccountNotActiveException;
import com.dvh.auth.security.CustomUserDetails;
import com.dvh.user.constants.UserStatus;
import com.dvh.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        String email = loginRequest.email().trim().toLowerCase();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, loginRequest.password())
        );


        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();//authentication.getPrincipal() se CustomUserDetails nikala aur .getUser() karke apni asli User entity le li.

        if(user.getUserStatus() != UserStatus.ACTIVE){
            log.warn("Login blocked for non-active account userId={} userStatus={} ",user.getId() , user.getUserStatus());
            throw new AccountNotActiveException("Account is not active");
        }

        String token = jwtService.generateAccessToken(user);
        log.info("Login successful userId={}",user.getId());


        return new LoginResponse(token,"Bearer",jwtService.getAccessTokenExpirySeconds());
    }
}
