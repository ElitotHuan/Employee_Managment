package com.example.Employee_Managment.repositories;

import com.example.Employee_Managment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUserId(Long userId);

    Boolean existsByUsername(String username);

    User findByUsername(String username);
}
