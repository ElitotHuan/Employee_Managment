package com.example.User_Managment.authenticate;

import com.example.User_Managment.exceptions_handler.customs_exception.AccountExpiredException;
import com.example.User_Managment.exceptions_handler.customs_exception.RolesAuthorizationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    private static final String JWT_SECRET_KEY = "TMASolution";

    @Autowired
    private TokenRepository tokenRepository;

    public String generateAuthToken(Token token) {
        //Set claims. Contain only necessary information of the user
        Map<String, Object> claims = new HashMap<>();
        claims.put("User-id", token.getUser().getUserId());
        claims.put("Role", token.getUser().getRole());

        //Create jwt token
        String authToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000))
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

    public Boolean checkRole(String authToken) {
        String role = Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken).getBody()
                .get("Role", String.class);

        if (role.equalsIgnoreCase("ADMIN")) {
            return true;
        } else {
            throw new RolesAuthorizationException("you don't have enough access privileges");
        }
    }

    private void create(Token token) {
        int updateCheck = tokenRepository.updateToken(token);
        if (updateCheck == 0) {
            tokenRepository.save(token);
        }
    }
}
