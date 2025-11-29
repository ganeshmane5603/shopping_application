package com.example.shopping_application.repository;

import com.example.shopping_application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPassword(String password);

    Optional<User> findByEmail(String email);

}
