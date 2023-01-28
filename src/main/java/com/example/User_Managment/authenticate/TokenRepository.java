package com.example.User_Managment.authenticate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

    @Modifying
    @Query(value = "Update token_info set created_date = :#{#token.created_date} , expired_date  = :#{#token.expired_date}" +
            ", access_token = :#{#token.accessToken} , refresh_token = :#{#token.refreshToken} where user_id = :#{#token.user.userId}", nativeQuery = true)
    @Transactional
    int updateToken(@Param("token") Token token);


    Token findByRefreshToken(String refreshToken);
}
