package com.dvh.user.mapper;

import com.dvh.user.dto.request.UserCreateRequest;
import com.dvh.user.dto.response.UserResponse;
import com.dvh.user.entity.User;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "userStatus", ignore = true)
    User toUserEntity(UserCreateRequest request);

    UserResponse toUserResponse(User user);
}
