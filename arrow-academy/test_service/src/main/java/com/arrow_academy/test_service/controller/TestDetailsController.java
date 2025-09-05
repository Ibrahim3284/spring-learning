package com.arrow_academy.test_service.controller;

import com.arrow_academy.test_service.model.TestDetails;
import com.arrow_academy.test_service.service.TestDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("testDetails")
public class TestDetailsController {

    @Autowired
    private TestDetailsService testDetailsService;

    @PostMapping("/add")
    public ResponseEntity<?> createTestDetails(@RequestHeader("Authorization") String token, @RequestBody TestDetails testDetails) {
        return testDetailsService.createTestDetails(token, testDetails);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTestDetails(@RequestHeader("Authorization") String token, @RequestBody TestDetails testDetails) {
        return testDetailsService.updateTestDetails(token, testDetails);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTestDetails(@RequestHeader("Authorization") String token) {
        return testDetailsService.getAllTestDetails(token);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getTestDetails(@RequestHeader("Authorization") String token, @RequestParam("title") String title, @RequestParam("date") Date date) {
        return testDetailsService.getTestDetails(token, title, date);
    }

}
