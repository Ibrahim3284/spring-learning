package com.ibrahim.spring_sec_demo.controller;

import com.ibrahim.spring_sec_demo.model.User;
import com.ibrahim.spring_sec_demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("register")
    public void register(@RequestBody User user) {
        service.saveUser(user);
    }
}
