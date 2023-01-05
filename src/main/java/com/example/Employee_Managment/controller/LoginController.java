package com.example.Employee_Managment.controller;

import com.example.Employee_Managment.jwtUtils.JwtTokenProvider;
import com.example.Employee_Managment.models.Login;
import com.example.Employee_Managment.models.Token;
import com.example.Employee_Managment.response_handler.error_response.Error;
import com.example.Employee_Managment.response_handler.error_response.ResponeObject;
import com.example.Employee_Managment.payload.LoginRequest;
import com.example.Employee_Managment.payload.LoginResponse;
import com.example.Employee_Managment.services.LoginService;
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
    private JwtTokenProvider jtp;

    private Token token = new Token();

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        Login account = loginService.findAccount(request.getUsername(), request.getPassword());

        if (account == null) {
            ResponeObject respone = new ResponeObject("Error found", new Error("Login failed",
                    "Username or Password is incorrect", HttpStatus.UNAUTHORIZED.value()));
            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
        } else {
            if (loginService.checkExpDate(account.getUsername())) {
                String authToken = token.generateAuthToken(account.getUser().getUserId());
                Token newToken = new Token(authToken, account.getUser());
                jtp.store(newToken);
                return ResponseEntity.status(200).body(new LoginResponse("Login success", authToken));
            } else {
                ResponeObject respone = new ResponeObject("Error found", new Error("Login failed",
                        "your account has expired please contact your system administrator", HttpStatus.UNAUTHORIZED.value()));
                return ResponseEntity.status(respone.getError().getStatus()).body(respone);
            }
        }
    }
}
