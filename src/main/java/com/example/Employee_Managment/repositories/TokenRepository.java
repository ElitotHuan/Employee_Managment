package com.example.Employee_Managment.repositories;

import com.example.Employee_Managment.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, String> {

//    @Query(value = "Select * from token_info t where t.user_id = :user_id" , nativeQuery = true)
//    Token findByUserId(@Param("employee_id") Long employee_id);


}
