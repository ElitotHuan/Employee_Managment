package com.example.User_Managment.Login.repositories;

import com.example.User_Managment.Login.models.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

    @Query(value = "Select * from login l " +
            "where l.username = :username and l.password = :password", nativeQuery = true)
    Login findAccount(@Param("username") String username, @Param("password") String password);


}
