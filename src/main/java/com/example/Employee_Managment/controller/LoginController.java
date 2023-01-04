package com.example.Employee_Managment.controller;

import com.example.Employee_Managment.models.Login;
import com.example.Employee_Managment.models.User;
import com.example.Employee_Managment.models.Token;
import com.example.Employee_Managment.response_handler.error_response.Error;
import com.example.Employee_Managment.response_handler.error_response.ResponeObject;
import com.example.Employee_Managment.payload.LoginRequest;
import com.example.Employee_Managment.payload.LoginResponse;
import com.example.Employee_Managment.services.LoginService;
import com.example.Employee_Managment.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;

    private Token token = new Token();

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Login login;
        String authToken;
        try {
            login = loginService.findAccount(request.getUsername(), request.getPassword());
            token.setUser(login.getUser_db());
            authToken = token.generateAuthToken();
        } catch (NullPointerException e) {
            ResponeObject respone = new ResponeObject("Error found", new Error("Login failed",
                    "Username or Password is wrong", HttpStatus.BAD_REQUEST.value()));
            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
        }


//        token.setToken(authToken);
//        token.setCreated_date(token.getCreateDateFromToken(authToken));
//        token.setExpired_date(token.getExpDateFromToken(authToken));
//        tokenService.store(token);
        return ResponseEntity.status(200).body(new LoginResponse("Login success", authToken));
    }
}
