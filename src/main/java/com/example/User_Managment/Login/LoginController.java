package com.example.User_Managment.Login;

import com.example.User_Managment.response_handler.ErrorRespone;

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

    private final Token token = new Token();

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Login.LoginRequest request) {
        Login account = loginService.findAccount(request.getUsername(), request.getPassword());

        if (account == null) {
            ErrorRespone respone = new ErrorRespone("Username or Password is incorrect", 401);
            return ResponseEntity.status(respone.getStatus()).body(respone);
        } else {
            if (!account.getExp_date().before(new Date())) {
                String authToken = token.generateAuthToken(account.getUser().getUserId());
                Token newToken = new Token(authToken, account.getUser());
                loginService.storeToken(newToken);
                return ResponseEntity.status(200).body(new Login.LoginRespone("Login success", authToken));
            }
            ErrorRespone respone = new ErrorRespone("your account has expired please contact your system administrator ", 401);
            return ResponseEntity.status(respone.getStatus()).body(respone);
        }
    }
}
