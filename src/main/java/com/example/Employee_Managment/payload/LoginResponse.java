package com.example.Employee_Managment.payload;

import lombok.Data;

@Data
public class LoginResponse {

    private String message;

    private String accessToken;

    public LoginResponse(String message, String accessToken) {
        this.message = message;
        this.accessToken = accessToken;
    }
}
