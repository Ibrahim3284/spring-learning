package com.arrow_academy.auth_service.services;

import com.arrow_academy.auth_service.model.User;
import com.arrow_academy.auth_service.dao.UserRepo;
import com.arrow_academy.auth_service.model.UserWrapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public ResponseEntity<String> saveUser(User user) {

        if(user.getRole().equals("admin")) return new ResponseEntity<>("Can't create admin user", HttpStatus.BAD_REQUEST);

        if(!repo.findAllByUsername(user.getUsername()).isEmpty()) return new ResponseEntity<>("User with this username already exist", HttpStatus.BAD_REQUEST);

        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    public ResponseEntity<Boolean> isStudent(UserWrapper user) {

        User user1 = repo.findByUsername(user.getUsername());
        return new ResponseEntity<>(user1.getRole().equals("student"), HttpStatus.OK);
    }

    public ResponseEntity<Boolean> isAdmin(UserWrapper user) {

        User user1 = repo.findByUsername(user.getUsername());
        return new ResponseEntity<>(user1.getRole().equals("admin"), HttpStatus.OK);
    }
}
