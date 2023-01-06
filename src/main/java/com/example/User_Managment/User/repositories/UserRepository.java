package com.example.User_Managment.User.repositories;

import com.example.User_Managment.User.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Select id ,age from user

    Boolean existsByUserId(Long userId);

    Boolean existsByUsername(String username);

    User findByUsername(String username);
}
