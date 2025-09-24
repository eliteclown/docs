package com.cybercity.application.advices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private HttpStatus status;
    private String error;
    private List<String> subErrors;
}
