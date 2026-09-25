package com.dvh.common.exception;

import com.dvh.common.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception exception,HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        "INTERVAL_SERVER_ERROR",
                        "Something went wrong",
                        request.getRequestURI()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException ex,
                                                               HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        "USER_NOT_FOUND",
                        ex.getMessage(),
                        req.getRequestURI()));
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ErrorResponse> invalidRoleException(InvalidRoleException ex,
                                                              HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        "INVALID_ROLE",
                        ex.getMessage(),
                        req.getRequestURI()));
    }

    @ExceptionHandler(VendorAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> vendorAlreadyExistsException(VendorAlreadyExistsException ex,
                                                                      HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        "VENDOR_ALREADY_EXISTS",
                        ex.getMessage(),
                        req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                         HttpServletRequest req){

        String collect = ex.getBindingResult()
                           .getFieldErrors()
                           .stream()
                           .map(error -> error.getField() + ": " + error.getDefaultMessage())
                           .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        "VALIDATION_ERROR",
                        collect,
                        req.getRequestURI()));

    }
}
