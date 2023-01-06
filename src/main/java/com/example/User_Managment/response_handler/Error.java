package com.example.User_Managment.response_handler;

import lombok.Data;

@Data
public class Error {
    private String message;


    private int status;

    public Error(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public Error(int status) {
        this.status = status;
    }

}
