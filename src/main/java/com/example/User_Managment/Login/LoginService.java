package com.example.User_Managment.Login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class LoginService {

    private final Logger logger = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public Login findAccount(String username, String password) {
        try {
            return loginRepository.findAccount(username, password);
        } catch (NullPointerException e) {
            logger.warn("Can't find account");
        }
        return null;
    }

    @Transactional
    public void storeToken(Token authToken) {
        int updateCheck = tokenRepository.updateToken(authToken);
        if (updateCheck == 0) {
            tokenRepository.save(authToken);
        }
    }

}
