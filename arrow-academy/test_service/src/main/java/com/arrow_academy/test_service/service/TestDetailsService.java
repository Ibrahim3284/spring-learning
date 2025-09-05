package com.arrow_academy.test_service.service;

import com.arrow_academy.test_service.dao.TestDetailsDao;
import com.arrow_academy.test_service.model.TestDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TestDetailsService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TestDetailsDao testDetailsDao;

    public ResponseEntity<?> createTestDetails(String token, TestDetails testDetails) {
        String role = String.valueOf(jwtService.parseTokenAsJSON(token).get("role"));

        if(!role.equals("faculty")) return new ResponseEntity<>("Only faculty can add test details", HttpStatus.FORBIDDEN);

        if(testDetailsDao.findByTitleAndDate(testDetails.getTitle(), testDetails.getDate()).isPresent()) return new ResponseEntity<>("Test with title " + testDetails.getTitle() + " on date " + testDetails.getDate() + " already exists", HttpStatus.BAD_REQUEST);

        testDetailsDao.save(testDetails);

        return new ResponseEntity<>("Test details created successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateTestDetails(String token, TestDetails testDetails) {
        String role = String.valueOf(jwtService.parseTokenAsJSON(token).get("role"));

        if(!role.equals("faculty")) return new ResponseEntity<>("Only faculty can add test details", HttpStatus.FORBIDDEN);

        if(!testDetailsDao.findByTitleAndDate(testDetails.getTitle(), testDetails.getDate()).isPresent()) return new ResponseEntity<>("Test with title " + testDetails.getTitle() + " on date " + testDetails.getDate() + " not present", HttpStatus.BAD_REQUEST);

        TestDetails testDetails1 = testDetailsDao.findByTitleAndDate(testDetails.getTitle(), testDetails.getDate()).get();
        testDetails1.setDuration(testDetails.getDuration());
        testDetails1.setTestWindow(testDetails.getTestWindow());
        testDetails1.setStartTime(testDetails.getStartTime());
        testDetailsDao.save(testDetails1);

        return new ResponseEntity<>("Test details updated successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllTestDetails(String token) {
        String role = String.valueOf(jwtService.parseTokenAsJSON(token).get("role"));

        if(!role.equals("student") && !role.equals("faculty")) return new ResponseEntity<>("Only faculty and students can see test details", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(testDetailsDao.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> getTestDetails(String token, String title, Date date) {
        String role = String.valueOf(jwtService.parseTokenAsJSON(token).get("role"));

        if(!role.equals("student") && !role.equals("faculty")) return new ResponseEntity<>("Only faculty and students can see test details", HttpStatus.FORBIDDEN);

        if(testDetailsDao.findByTitleAndDate(title, date).isEmpty()) return new ResponseEntity<>("Test doesnt exist", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(testDetailsDao.findByTitleAndDate(title, date).get(), HttpStatus.OK);
    }
}
