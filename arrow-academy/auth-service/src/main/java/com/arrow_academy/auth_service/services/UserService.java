package com.arrow_academy.auth_service.services;

import com.arrow_academy.auth_service.model.User;
import com.arrow_academy.auth_service.dao.UserRepo;
import com.arrow_academy.auth_service.model.UserWrapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public int fetchUserCountByUsername(String username) {
        return repo.findAllByUsername(username).size();
    }

    @Transactional
    public ResponseEntity<String> createUser(User user) {

        if(user.getRole().equals("admin")) return new ResponseEntity<>("Can't create admin user", HttpStatus.BAD_REQUEST);

        if(!repo.findAllByUsername(user.getUsername()).isEmpty()) return updateUser(user);

        if(user.getPassword() != null) user.setPassword(encoder.encode(user.getPassword()));
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

    public ResponseEntity<Boolean> isFaculty(UserWrapper user) {
        User user1 = repo.findByUsername(user.getUsername());
        return new ResponseEntity<>(user1.getRole().equals("faculty"), HttpStatus.OK);
    }

    public ResponseEntity<String> updateUser(User user) {
        User currUser = repo.findByUsername(user.getUsername());
        if(user.getPassword() != null) {
            if(currUser.getPassword() != null) return new ResponseEntity<>("Password is already set", HttpStatus.OK);
            else {
                currUser.setPassword(encoder.encode(user.getPassword()));
            }
        } else {
            currUser.setRole(user.getRole());
        }
        repo.save(currUser);
        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }
}
