package com.example.User_Managment.Login.controller;

import com.example.User_Managment.Authenticate.service.JwtTokenProvider;
import com.example.User_Managment.Authenticate.models.Token;
import com.example.User_Managment.Login.models.Login;
import com.example.User_Managment.response_handler.Error;
import com.example.User_Managment.response_handler.ResponeObject;

import com.example.User_Managment.Login.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtTokenProvider jtp;

    private Token token = new Token();

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Login.LoginRequest request) {

        Login account = loginService.findAccount(request.getUsername(), request.getPassword());

        if (account == null) {
            ResponeObject respone = new ResponeObject("Error found",
                    new Error("Username or Password is incorrect", 401));
            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
        } else {
            if (!account.getExp_date().before(new Date())) {
                String authToken = token.generateAuthToken(account.getUser().getUserId());
                Token newToken = new Token(authToken, account.getUser());
                jtp.store(newToken);
                return ResponseEntity.status(200).body(new Login.LoginRespone("Login success", authToken));
            } else {
                ResponeObject respone = new ResponeObject("Error found",
                        new Error("your account has expired please contact your system administrator", 403));
                return ResponseEntity.status(respone.getError().getStatus()).body(respone);
            }
        }
    }
}
