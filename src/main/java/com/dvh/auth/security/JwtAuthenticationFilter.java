package com.dvh.auth.security;

import com.dvh.auth.service.JwtService;
import com.dvh.user.constants.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);

        try {
            Claims claims = jwtService.parseToken(token);
            String email = claims.get("email", String.class);

            if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                CustomUserDetails userDetails =(CustomUserDetails) userDetailsService.loadUserByUsername(email);

                if(userDetails.getUser().getUserStatus() == UserStatus.ACTIVE){
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));   // attach IP and session info
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }catch (ExpiredJwtException e){
            request.setAttribute("auth.error","TOKEN_EXPIRED");// its stores this key/value in the current http request obj and forwards

        }catch (JwtException | IllegalArgumentException | UsernameNotFoundException e){
            request.setAttribute("auth.error", "TOKEN_INVALID");
        }

        filterChain.doFilter(request,response);
    }
}
