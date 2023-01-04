package com.example.Employee_Managment.response_handler.error_response;

import lombok.Data;

@Data
public class ResponeObject {

    private String message;
    private Error error;

    public ResponeObject(String message, Error error) {
        this.message = message;
        this.error = error;
    }


    public ResponeObject(String message) {
        this.message = message;
        this.error = new Error(200);
    }

}
