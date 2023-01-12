package com.example.User_Managment.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Boolean existsByUserId(Long userId);

    Boolean existsByUsername(String username);

    @Query(value = "select new com.example.User_Managment.User.UserDTO (u.userId , u.name, u.age , u.position , u.salary) from User u")
    List<UserDTO> getAllUsers();

}
