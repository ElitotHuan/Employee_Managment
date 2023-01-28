package com.example.User_Managment.exceptions_handler.customs_exception;

public class UserExistedException extends RuntimeException {
    public UserExistedException(String message){
        super(message);
    }
}
