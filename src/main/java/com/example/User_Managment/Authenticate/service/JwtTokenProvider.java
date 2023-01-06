package com.example.User_Managment.Authenticate.service;

import com.example.User_Managment.Authenticate.models.Token;
import com.example.User_Managment.Authenticate.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwtTokenProvider {

    @Autowired
    private TokenRepository tokenRepository;

    @Transactional
    public void store(Token token) {
        int updateCheck = tokenRepository.updateToken(token);
        if (updateCheck == 0) { tokenRepository.save(token); }
    }

}
