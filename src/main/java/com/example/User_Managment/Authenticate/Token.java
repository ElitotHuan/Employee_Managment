package com.example.User_Managment.Authenticate;

import com.example.User_Managment.User.User;
import io.jsonwebtoken.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;


@Data
@Entity
@Table(name = "Token_info")
public class Token {
    private static final String JWT_SECRET_KEY = "TMASolution";
    private TokenRepository tokenRepository;

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

    public Token(String token, User user) {
        this.token = token;
        this.created_date = new Date(System.currentTimeMillis());
        this.expired_date = new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000);
        this.user = user;
    }

    public Token(User user) {
        this.user = user;
    }

    public String generateAuthToken(Token token) {
        //Set claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("Token_content", token);

        //Create jwt token
        String authToken =  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY).compact();
        create(token);

        return authToken;
    }

    public Boolean validateToken(String authToken) {
        Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken).getBody();
        return true;
    }

    public Long getUserIdFromToken(String authToken) {
        return getClaimsFromToken(authToken).get("user-id", Long.class);
    }

    private Claims getClaimsFromToken(String authToken) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken).getBody();
    }

    private void create(Token t) {
        int updateChekc = tokenRepository.updateToken(t);
        if (updateChekc == 0) {
            tokenRepository.save(t);
        }
    }
    
}
