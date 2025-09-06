package com.arrow_academy.test_service.controller;

import com.arrow_academy.test_service.model.*;
import com.arrow_academy.test_service.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private TestService testService;

    @PostMapping("add")
    public ResponseEntity<?> addTest(@RequestHeader("Authorization") String token, @RequestParam("title") String title, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam("subject") String subject, @RequestPart("questions") List<Question> questions, @RequestPart("images") List<MultipartFile> images) throws IOException {
        return testService.addTest(token, title, date, subject, questions, images);
    }

    @GetMapping("get/questions")
    public ResponseEntity<?> getQuestions(@RequestHeader("Authorization") String token, @RequestParam("title") String title, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return testService.getQuestions(token, title, date);
    }

    @PostMapping("attempt")
    public ResponseEntity<?> attemptTest(@RequestHeader("Authorization") String token, @RequestParam("title") String title, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return testService.attemptTest(token, title, date);
    }

    @PostMapping("save")
    public ResponseEntity<?> saveTest(@RequestHeader("Authorization") String token, @RequestParam("title") String title, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestBody List<Response> responses) throws JsonProcessingException {
        return testService.saveTest(token, title, date, responses);
    }

    @PostMapping("submit")
    public ResponseEntity<?> submitTest(@RequestHeader("Authorization") String token, @RequestParam("title") String title, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestBody List<Response> responses) throws JsonProcessingException {
        return testService.submitTest(token, title, date, responses);
    }

    @GetMapping("responses")
    public ResponseEntity<?> getResponses(@RequestHeader("Authorization") String token, @RequestParam("title") String title, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws JsonProcessingException {
        return testService.getResponses(token, title, date);
    }
}
