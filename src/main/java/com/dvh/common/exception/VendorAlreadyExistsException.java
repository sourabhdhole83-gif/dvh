package com.dvh.common.exception;


public class VendorAlreadyExistsException extends RuntimeException{
    public VendorAlreadyExistsException(String message){
        super(message);
    }
}
