package com.example.Employee_Managment.jwtUtils;

import com.example.Employee_Managment.models.Token;
import com.example.Employee_Managment.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenProvider {

    @Autowired
    private TokenRepository tokenRepository;

    public Token store(Token token) {
        Token checkToken = tokenRepository.findByUserId(token.getUser().getUserId());
        if (checkToken != null) {
            checkToken.setToken(token.getToken());
            checkToken.setCreated_date(token.getCreated_date());
            checkToken.setExpired_date(token.getExpired_date());
            return tokenRepository.save(checkToken);
        } else {
            return tokenRepository.save(token);
        }
    }

}
