package com.example.shopping_application.repository;

import com.example.shopping_application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPassword(String password);

    User findByEmail(String email);

}
