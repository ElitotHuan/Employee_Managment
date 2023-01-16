package com.example.User_Managment.Login;

import com.example.User_Managment.Authenticate.Token;
import com.example.User_Managment.response_handler.ErrorRespone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody Login.LoginRequest request) {
        Object account = loginService.validateAccount(request);

        if (account == null) {
            return ResponseEntity.badRequest().body(new ErrorRespone("Username or password is incorrect" , 400));
        }

        if ((Boolean) account == false) {
            return ResponseEntity.status(403).body(new ErrorRespone("Your account has expired please contact your system administrator" , 403));
        }

        Login a = (Login) account;
        Token newToken = new Token(a.getUser());
        String authToken = token.generateAuthToken(newToken);
        return ResponseEntity.status(200).body(new Login.LoginRespone("Login success", authToken));

    }
}
