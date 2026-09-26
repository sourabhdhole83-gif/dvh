package com.dvh.user.service;

import com.dvh.common.exception.UserNotFoundException;
import com.dvh.user.dto.request.UserCreateRequest;
import com.dvh.user.dto.response.UserResponse;
import com.dvh.user.entity.User;
import com.dvh.user.exception.UserAlreadyExistsException;
import com.dvh.user.mapper.UserMapper;
import com.dvh.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest userCreateRequest){
        if(userRepository.existsByEmail(userCreateRequest.getEmail())){
            throw new UserAlreadyExistsException("Email is already registered: "+userCreateRequest.getEmail());
        }
        if(userRepository.existsByPhone(userCreateRequest.getPhone())){
            throw new UserAlreadyExistsException("Phone is  already registered: "+userCreateRequest.getPhone());
        }
        User user = mapper.toUserEntity(userCreateRequest);
        user.setPassword(encoder.encode(userCreateRequest.getPassword()));

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
