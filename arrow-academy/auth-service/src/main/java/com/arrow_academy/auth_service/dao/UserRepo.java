package com.arrow_academy.auth_service.dao;

import com.arrow_academy.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {

    User findByUsername(String username);
    List<User> findAllByUsername(String username);
}
