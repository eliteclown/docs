package com.cybercity.application.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
