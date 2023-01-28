package com.example.User_Managment.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    @Query(value = "select new com.example.User_Managment.user.UserDTO (u.userId , u.name, u.age , u.position , u.salary) " +
            "from User u")
    List<UserDTO> getAllUsers();



}
