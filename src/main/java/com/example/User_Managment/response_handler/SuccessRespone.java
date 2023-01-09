package com.example.User_Managment.response_handler;

import lombok.Data;

@Data
public class SuccessRespone {

    private String message;

    public SuccessRespone(String message) {
        this.message = message;
    }

}
