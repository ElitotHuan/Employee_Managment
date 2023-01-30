package com.example.User_Managment.authenticate;

import com.example.User_Managment.exceptions_handler.customs_exception.RefreshTokenException;
import com.example.User_Managment.exceptions_handler.customs_exception.RolesAuthorizationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {
    private static final String JWT_SECRET_KEY = "TMASolution";

    @Autowired
    private TokenRepository tokenRepository;


    //Generate new access token and refresh token when login
    public Token generateJwtToken(Token token) {
        String authToken = this.generate(token);

        //Set token infomation and save to database
        token.setAccessToken(authToken);
        token.setRefreshToken(UUID.randomUUID().toString());
        token.setCreated_date(new Date(System.currentTimeMillis()));
        token.setExpired_date(new Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000));
        this.create(token);

        return token;
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


    //Generate a new access token when refresh token hasn't expired yet
    public Token verifyExpiration(String requestRefreshToken) {
        Token t = tokenRepository.findByRefreshToken(requestRefreshToken);
        if (t == null) {
            throw new RefreshTokenException(requestRefreshToken + " Refresh token is not in database");
        }

        if (!t.getExpired_date().before(new Date())) {
            String newAccessToken = this.generate(t);
            t.setAccessToken(newAccessToken);
            this.create(t);
            return t;
        } else {
            throw new RefreshTokenException(requestRefreshToken + " Refresh token was expired. Please make a new signin request");
        }
    }

    private String generate(Token token) {
        //Set claims. Contain only necessary information of the user
        Map<String, Object> claims = new HashMap<>();
        claims.put("User-id", token.getUser().getUserId());
        claims.put("Role", token.getUser().getRole());

        //Create jwt token
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY).compact();
    }

    private int create(Token token) {
        int updateCheck = tokenRepository.updateToken(token);
        if (updateCheck == 0) {
            tokenRepository.save(token);
            return 0;
        }
        return 1;
    }


}
