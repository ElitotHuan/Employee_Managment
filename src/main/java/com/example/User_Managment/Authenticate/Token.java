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

    @Id
    @Column(name = "id", columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Token_id", columnDefinition = "TEXT")
    private String token;

    @NotNull
    private Date created_date;

    @NotNull
    private Date expired_date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public Token() {
    }

    public Token(String token, User user) {
        this.token = token;
        this.created_date = getClaimsFromToken(token).getIssuedAt();
        this.expired_date = getClaimsFromToken(token).getExpiration();
        this.user = user;
    }

    public String generateAuthToken(Long user_id) {
        //Set claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("user-id", user_id);

        //Create jwt token
        return Jwts.builder()
                .setSubject("Token")
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY).compact();
    }

    public Jws<Claims> validateToken(String authToken) {
        Jws<Claims> jwt = Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken);
        return jwt;
    }

    public Long getUserIdFromToken(String authToken) {
        return getClaimsFromToken(authToken).get("user-id", Long.class);
    }

    private Claims getClaimsFromToken(String authToken) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken).getBody();
    }
}
