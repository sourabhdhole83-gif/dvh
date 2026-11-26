package com.dvh.auth.security;

import com.dvh.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service                                                // the only UserDetailsService bean, so Spring Security finds it automatically
@RequiredArgsConstructor                                // constructor injection for the final field below
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;        // existing repository, findByEmail already exists

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)                         // one SELECT ... WHERE email = ?
                .map(CustomUserDetails::new)                             // wrap the entity in our adapter
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); // provider turns this into BadCredentialsException (no user enumeration)
    }
}