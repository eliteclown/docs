package com.cybercity.application.exceptions;

public class RegistrationFailException extends RuntimeException{
    public  RegistrationFailException(String message){
        super(message);
    }
}
