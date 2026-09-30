package com.dvh.user.exception;


import lombok.*;

import java.util.*;

@Getter
public class UserAlreadyExistsException extends RuntimeException{
    private final Map<String,String> errors;

    public UserAlreadyExistsException( Map<String,String> errors) {
        super("User already exists");
        this.errors = errors;
    }
}
