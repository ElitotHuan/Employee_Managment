package com.example.Employee_Managment.response_handler.error_response;

import lombok.Data;

@Data
public class Error {
    private String message;

    private String cause;

    private int status;

    public Error(String message, String cause, int status) {
        this.message = message;
        this.cause = cause;
        this.status = status;
    }

    public Error(int status) {
        this.status = status;
    }

}
