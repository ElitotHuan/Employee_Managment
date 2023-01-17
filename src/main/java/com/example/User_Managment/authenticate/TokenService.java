package com.example.User_Managment.authenticate;

import com.example.User_Managment.configuration.SpringConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    private static final String JWT_SECRET_KEY = "TMASolution";
    public String generateAuthToken(Token token) {
        //Set claims. Contain only necessary information of the user
        Map<String, Object> claims = new HashMap<>();
        claims.put("User-id", token.getUser().getUserId());
        claims.put("Role", token.getUser().getRole());

        //Create jwt token
        String authToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY).compact();

        //Set token infomation before save to database
        token.setToken(authToken);
        token.setCreated_date(new Date(System.currentTimeMillis()));
        token.setExpired_date(new Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000));
        this.create(token);

        return authToken;
    }

    public Boolean validateToken(String authToken) {
        Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken).getBody();
        return true;
    }

    public String getRoleFromToken(String authToken) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken).getBody()
                .get("Role", String.class);
    }


    private void create(Token token) {
        TokenRepository tokenRepository = (TokenRepository) SpringConfiguration.contextProvider().getApplicationContext().getBean("tokenRepository");
        int updateCheck = tokenRepository.updateToken(token);
        if (updateCheck == 0) {
            tokenRepository.save(token);
        }
    }
}
