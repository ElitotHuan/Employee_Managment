package com.example.User_Managment.Login.services;

import com.example.User_Managment.Login.models.Login;
import com.example.User_Managment.Login.repositories.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final Logger logger = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    LoginRepository loginRepository;

    public Login findAccount(String username, String password) {
        try {
            Login account = loginRepository.findAccount(username, password);
            return account;
        } catch (NullPointerException e) {
            logger.error("Can't find account");
        }
        return null;
    }

}
