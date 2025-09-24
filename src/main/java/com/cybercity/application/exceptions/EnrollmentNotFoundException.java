package com.cybercity.application.exceptions;

public class EnrollmentNotFoundException extends RuntimeException{
    public EnrollmentNotFoundException(String message){
        super(message);
    }
}
