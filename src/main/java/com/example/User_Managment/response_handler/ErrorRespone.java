package com.example.User_Managment.response_handler;

import lombok.Data;

@Data
public class ErrorRespone {
    private String message;
    private int status;

    public ErrorRespone(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
