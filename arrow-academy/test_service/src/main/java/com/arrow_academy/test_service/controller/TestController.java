package com.arrow_academy.test_service.controller;

import com.arrow_academy.test_service.model.*;
import com.arrow_academy.test_service.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private TestService testService;

    @PostMapping("add")
    public ResponseEntity<String> addTest(@RequestHeader("Authorization") String token, @RequestParam("title") String title, @RequestParam String subject, @RequestParam("start_time") String startTime, @RequestParam("duration") int duration, @RequestPart List<Question> questionList, @RequestPart List<MultipartFile> imageFiles) throws IOException, ParseException {
        return testService.addTest(token, title, subject, startTime, duration, questionList, imageFiles);
    }

    @GetMapping("get")
    public ResponseEntity<?> getTest(@RequestHeader("Authorization") String token, @RequestParam String title, @RequestParam String subject){
        return testService.getTest(token, title, subject);
    }

    @GetMapping("getAll")
    public ResponseEntity<?> getAllTests(@RequestHeader("Authorization") String token) {
        return testService.getAllTests(token);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getAllQuestionsForATest(@RequestHeader("Authorization") String token, @PathVariable("id") int id){
        return testService.getAllQuestionsForATest(token, id);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<?> submitTest(@PathVariable("id") int id, @RequestHeader("Authorization") String token, @RequestBody List<Response> responses) throws IOException, URISyntaxException, InterruptedException {
        return testService.submitTest(id, token, responses);
    }
}
