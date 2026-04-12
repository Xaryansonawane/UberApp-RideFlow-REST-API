package com.example.UberApp.repository;

import com.example.UberApp.model.User;
import com.example.UberApp.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔍 Login
    Optional<User> findByEmail(String email);

    // 👥 Users by role
    List<User> findByRole(Role role);

    // 🚫 BLOCKED USERS
    List<User> findByBlockedUntilAfter(LocalDateTime now);

    // 🚗 BLOCKED BY ROLE
    List<User> findByRoleAndBlockedUntilAfter(Role role, LocalDateTime now);
}