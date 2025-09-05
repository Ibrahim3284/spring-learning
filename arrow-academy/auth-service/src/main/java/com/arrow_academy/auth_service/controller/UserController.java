package com.arrow_academy.auth_service.controller;

import com.arrow_academy.auth_service.model.ResetPassword;
import com.arrow_academy.auth_service.model.User;
import com.arrow_academy.auth_service.model.UserDto;
import com.arrow_academy.auth_service.model.UserWrapper;
import com.arrow_academy.auth_service.services.JwtService;
import com.arrow_academy.auth_service.services.PasswordResetService;
import com.arrow_academy.auth_service.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        return service.createUser(userDto);
    }

    @PostMapping("admin/register")
    public ResponseEntity<String> adminRegister(@RequestHeader("Authorization") String token, @Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        if(service.fetchUserCountByUsername(user.getUsername()) == 0) return service.createUserByAdmin(token, user);
        else return service.updateUserByAdmin(token, user);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserWrapper user) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if(authentication.isAuthenticated()) return new ResponseEntity<>(jwtService.generateToken(user), HttpStatus.OK);
        else return new ResponseEntity<>("Login failed", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("isStudent")
    public ResponseEntity<Boolean> isStudent(@RequestBody UserWrapper user) {

        return service.isStudent(user);
    }

    @PostMapping("isAdmin")
    public ResponseEntity<Boolean> isAdmin(@RequestBody UserWrapper user) {

        return service.isAdmin(user);
    }

    @PostMapping("isFaculty")
    public ResponseEntity<Boolean> isFaculty(@RequestBody UserWrapper user) {

        return service.isFaculty(user);
    }

    @PostMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        return passwordResetService.sendEmail(email);
    }

    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("otp") Integer otp, @RequestBody ResetPassword resetPassword) {
        return passwordResetService.resetPassword(resetPassword, otp);
    }
}
