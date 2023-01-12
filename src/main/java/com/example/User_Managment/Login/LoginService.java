package com.example.User_Managment.Login;

import com.example.User_Managment.Authenticate.Token;
import com.example.User_Managment.Authenticate.TokenRepository;
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

    public Login findAccount(Login.LoginRequest request) {
        Login account = null;
        try {
            account = loginRepository.findAccount(request.getUsername(), request.getPassword());
        } catch (NullPointerException e) {
            logger.warn("Can't find account");
        }

        if (!account.getExp_date().before(new Date())) {
            return account;
        }
        return null;
    }
}
