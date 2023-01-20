package com.example.User_Managment.authenticate;

import com.example.User_Managment.user.User;
import io.jsonwebtoken.*;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "Token_info")
public class Token {



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
        this.user = user;
    }

//    public String generateAuthToken(Token token) {
//        //Set claims. Contain only necessary information of the user
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("User-id", token.getUser().getUserId());
//        claims.put("Role", token.getUser().getRole());
//
//        //Create jwt token
//        String authToken = Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000))
//                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY).compact();
//
//        //Set token infomation before save to database
//        token.setToken(authToken);
//        token.setCreated_date(new Date(System.currentTimeMillis()));
//        token.setExpired_date(new Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000));
//        this.create(token);
//
//        return authToken;
//    }
//
//    public Boolean validateToken(String authToken) {
//        Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken).getBody();
//        return true;
//    }
//
//    public String getRoleFromToken(String authToken) {
//        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken).getBody()
//                .get("Role", String.class);
//    }
//
//
//    private void create(Token token) {
//        TokenRepository tokenRepository = (TokenRepository) SpringConfiguration.contextProvider().getApplicationContext().getBean("tokenRepository");
//        int updateCheck = tokenRepository.updateToken(token);
//        if (updateCheck == 0) {
//            tokenRepository.save(token);
//        }
//    }

}
