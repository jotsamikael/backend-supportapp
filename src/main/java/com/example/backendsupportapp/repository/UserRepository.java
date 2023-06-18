package com.example.backendsupportapp.repository;

import com.example.backendsupportapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserName(String username);
    User findUserByEmail(String email);




}
