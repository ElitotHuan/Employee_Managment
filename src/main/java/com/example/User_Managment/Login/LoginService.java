package com.example.User_Managment.Login;

import com.example.User_Managment.Authenticate.Token;
import com.example.User_Managment.Authenticate.TokenRepository;
import com.example.User_Managment.response_handler.ErrorRespone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LoginService {

    private final Logger logger = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    private LoginRepository loginRepository;

    public Object validateAccount(Login.LoginRequest request) {
        logger.info("Find and validate account...");
        Login account;
        try {
            account = loginRepository.validateAccount(request.getUsername(), request.getPassword());
        } catch (NullPointerException e) {
            return null;
        }

        if (!account.getExp_date().before(new Date())) {
            return account;
        }
        return false;
    }
}
