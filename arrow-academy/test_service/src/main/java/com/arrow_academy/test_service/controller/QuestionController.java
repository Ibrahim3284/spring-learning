package com.arrow_academy.test_service.controller;

import com.arrow_academy.test_service.model.Question;
import com.arrow_academy.test_service.model.QuestionWrapper;
import com.arrow_academy.test_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("add")
    public ResponseEntity<String> addQuestion(@RequestPart Question question, @RequestPart MultipartFile imageFile) throws IOException {
        return questionService.addQuestion(question, imageFile);
    }

    @PostMapping("addMultiple")
    public ResponseEntity<String> addMultipleQuestion(@RequestPart List<Question> questions, @RequestPart("imageFiles") List<MultipartFile> imageFiles) throws IOException {
        System.out.println("Imagefiles: " + imageFiles.size());
        return questionService.addMultipleQuestions(questions, imageFiles);
    }

    @GetMapping("getAllQuestions")
    public ResponseEntity<List<QuestionWrapper>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("getAllQuestionsRaw")
    public ResponseEntity<List<Question>> getAllQuestionsRaw() {
        return questionService.getAllQuestionsRaw();
    }

}
