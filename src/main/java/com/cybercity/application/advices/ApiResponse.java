package com.cybercity.application.advices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse <T>{
    private LocalDateTime timestamp;
    private T data;
    private ApiError apiError;

    public ApiResponse(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(T data){
        this();
        this.data=data;
    }

    public ApiResponse(ApiError apiError){
        this();
        this.apiError=apiError;
    }
}
