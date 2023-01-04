package com.example.Employee_Managment.repositories;

import com.example.Employee_Managment.models.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

    @Query(value = "Select * from login l " +
            "where l.username = :username and l.password = :password and l.exp_date > now()" , nativeQuery = true)
    Login findByUsernameAndPasssword(@Param("username") String username, @Param("password") String password);


    @Query(value = "Select * from login l where l.user_id =: userId", nativeQuery = true)
    Login findByUserId(@Param("userId") Long userId);
}
