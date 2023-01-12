package com.example.User_Managment.response_handler;

import lombok.Data;

@Data
public class ErrorRespone {
    private String message;
    private int statusCode;

    public ErrorRespone(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
