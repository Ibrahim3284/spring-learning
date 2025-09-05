package com.arrow_academy.auth_service.services;

import com.arrow_academy.auth_service.model.User;
import com.arrow_academy.auth_service.dao.UserRepo;
import com.arrow_academy.auth_service.model.UserDto;
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

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public int fetchUserCountByUsername(String username) {
        return repo.findAllByUsername(username).size();
    }

    @Transactional
    public ResponseEntity<String> createUser(UserDto userDto) {

        if(!userDto.getRole().equals("normal_user")) return new ResponseEntity<>("Can't create user other than normal_user", HttpStatus.FORBIDDEN);

        if(!userDto.getConfirmPassword().equals(userDto.getPassword())) return new ResponseEntity<>("Confirm password and passwords field dont match", HttpStatus.BAD_REQUEST);
        if(!repo.findAllByUsername(userDto.getUsername()).isEmpty()) {
            User user = repo.findByUsername(userDto.getUsername());
            return updateUser(user);
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        repo.save(user);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> createUserByAdmin(String token, User user) {

        if(jwtService.parseTokenAsJSON(token.substring(7)).get("role").equals("admin")) {
            if (user.getRole().equals("admin"))
                return new ResponseEntity<>("Can't create admin user", HttpStatus.FORBIDDEN);

            if (!repo.findAllByUsername(user.getUsername()).isEmpty()) return updateUserByAdmin(token, user);

            if (user.getPassword() != null) user.setPassword(encoder.encode(user.getPassword()));
            repo.save(user);
            return new ResponseEntity<>("User created", HttpStatus.CREATED);
        } else return new ResponseEntity<>("Cant create user", HttpStatus.FORBIDDEN);
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
        }
        repo.save(currUser);
        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }

    public ResponseEntity<String> updateUserByAdmin(String token, User user) {
        if(jwtService.parseTokenAsJSON(token.substring(7)).get("role").equals("admin")) {
            User currUser = repo.findByUsername(user.getUsername());
            if (user.getPassword() != null) {
                if (currUser.getPassword() != null)
                    return new ResponseEntity<>("Password is already set", HttpStatus.OK);
                else {
                    currUser.setPassword(encoder.encode(user.getPassword()));
                }
            } else {
                currUser.setRole(user.getRole());
            }
            repo.save(currUser);
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        } else return new ResponseEntity<>("Cant update user", HttpStatus.FORBIDDEN);
    }
}
