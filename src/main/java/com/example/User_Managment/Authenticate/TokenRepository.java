package com.example.User_Managment.Authenticate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> { ;

    @Modifying
    @Query(value = "Update token_info  set created_date = :#{#token.created_date} , expired_date  = :#{#token.expired_date}" +
            ", token_id = :#{#token.token} where user_id = :#{#token.user.userId}", nativeQuery = true)
    int updateToken(@Param("token") Token token);

}
