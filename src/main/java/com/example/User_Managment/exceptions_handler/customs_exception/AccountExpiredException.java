package com.example.User_Managment.exceptions_handler.customs_exception;

public class AccountExpiredException extends RuntimeException {

    public AccountExpiredException(String message) {
        super(message);
    }
}
