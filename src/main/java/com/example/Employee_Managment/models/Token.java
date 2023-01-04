package com.example.Employee_Managment.models;


import com.example.Employee_Managment.jwtUtils.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Data
@Validated
@Entity
@Table(name = "Token_info")
public class Token {
    private static final String JWT_SECRET_KEY = "TMASolution";


    @Id
    @Column(name = "id",columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Token_id", columnDefinition = "TEXT")
    private String token;
    @NotNull
    private Date created_date;
    @NotNull
    private Date expired_date;

    @OneToOne(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;



    public Token() {
    }

    public Token(String token, Date created_date, Date expired_date, User user) {
        this.token = token;
        this.created_date = created_date;
        this.expired_date = expired_date;
        this.user = user;
    }

    public String generateAuthToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user-id", user.getUserId());

        //Create jwt token
        return Jwts.builder()
                .setSubject("Token")
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY).compact();
    }



    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken).getBody();
            return true;
        } catch (ExpiredJwtException e) {

        }

        return false;
    }


}
