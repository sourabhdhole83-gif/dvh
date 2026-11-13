package com.dvh.common.exception;

import com.dvh.common.ErrorResponse;
import com.dvh.user.exception.UserAlreadyExistsException;
import com.dvh.vendor.exception.VendorAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception exception,HttpServletRequest request){
        log.error("Unhandled exception at {}",request.getRequestURI(),exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        "INTERNAL_SERVER_ERROR",
                        "Something went wrong",
                        request.getRequestURI(),
                        null));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException ex,
                                                               HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        "USER_NOT_FOUND",
                        ex.getMessage(),
                        req.getRequestURI(),
                        null));
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ErrorResponse> invalidRoleException(InvalidRoleException ex,
                                                              HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        "INVALID_ROLE",
                        ex.getMessage(),
                        req.getRequestURI(),
                        null));
    }

    @ExceptionHandler(VendorAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> vendorAlreadyExistsException(VendorAlreadyExistsException ex,
                                                                      HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        "VENDOR_ALREADY_EXISTS",
                        ex.getMessage(),
                        req.getRequestURI(),
                        null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                         HttpServletRequest req){

        Map<String,String> errors = new LinkedHashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error->
                        errors.put(error.getField(),error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        "VALIDATION_ERROR",
                        "Validation failed",
                        req.getRequestURI(),
                        errors));

    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExistsException(UserAlreadyExistsException ex,HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        "DUPLICATE_ENTRY",
                        ex.getMessage(),
                        req.getRequestURI(),
                        ex.getErrors()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException ex,HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        "BAD_REQUEST",
                        ex.getMessage(),
                        req.getRequestURI(),
                        null));
    }
}
