package com.ibrahim.spring_sec_demo.services;

import com.ibrahim.spring_sec_demo.dao.UserRepo;
import com.ibrahim.spring_sec_demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }
}
