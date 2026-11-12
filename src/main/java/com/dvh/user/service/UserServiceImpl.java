package com.dvh.user.service;

import com.dvh.common.exception.InvalidRoleException;
import com.dvh.common.exception.UserNotFoundException;
import com.dvh.user.constants.Role;
import com.dvh.user.dto.request.UserCreateRequest;
import com.dvh.user.dto.response.UserResponse;
import com.dvh.user.entity.User;
import com.dvh.user.exception.UserAlreadyExistsException;
import com.dvh.user.mapper.UserMapper;
import com.dvh.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest userCreateRequest){
        if(userCreateRequest.getRole()== Role.ADMIN){
            throw new InvalidRoleException( "ADMIN role cannot be created through public registration");
        }
        Map<String,String> errors = new LinkedHashMap<>();
        if(userRepository.existsByEmail(userCreateRequest.getEmail())){
            log.warn("Duplicate registration attempt for email={}", userCreateRequest.getEmail());
            errors.put("email","Email is already registered");
        }
        if(userRepository.existsByPhone(userCreateRequest.getPhone())){
            log.warn("Duplicate registration attempt for phone={}",userCreateRequest.getPhone());
            errors.put("phone","Phone is already registered");
        }
        if(!errors.isEmpty()){
            throw new UserAlreadyExistsException(errors);
        }

        User user = mapper.toUserEntity(userCreateRequest);
        user.setPasswordHash(encoder.encode(userCreateRequest.getPassword()));

        User savedUser = userRepository.save(user);
        return mapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return mapper.toUserResponse(user);
    }
}
