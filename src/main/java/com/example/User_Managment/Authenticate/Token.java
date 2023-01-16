package com.example.User_Managment.Authenticate;

import com.example.User_Managment.User.User;
import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "Token_info")
public class Token {
    private static final String JWT_SECRET_KEY = "TMASolution";


    @Id
    @Column(name = "id", columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Token_id", columnDefinition = "TEXT")
    private String token;

    private Date created_date;

    private Date expired_date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public Token() {
    }

    public Token(User user) {
        this.created_date = new Date(System.currentTimeMillis());
        this.expired_date = new Date(System.currentTimeMillis() + 10 * 60 * 60 * 1000);
        this.user = user;
    }


    public String generateAuthToken(Token token) {
        //Set claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("user-id", token.user.getUserId());

        //Create jwt token
        String authToken =  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY).compact();


        //Add or Update token to the database
        token.setToken(authToken);
//        create(token);

        return authToken;
    }

    public Boolean validateToken(String authToken) {
        Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken).getBody();
        return true;
    }

    
}
