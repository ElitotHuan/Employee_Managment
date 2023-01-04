package com.example.Employee_Managment.payload;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@Validated
public class LoginRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
