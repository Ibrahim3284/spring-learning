package com.ibrahim.quizapp.service;

import com.ibrahim.quizapp.model.Question;
import com.ibrahim.quizapp.repo.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepo questionRepo;

    public List<Question> getAllQuestions() {

        return questionRepo.findAll();
    }

    public List<Question> getQuestionsByCategory(String category) {

        return questionRepo.findByCategory(category);
    }

    public String addQuestion(Question question) {
        questionRepo.save(question);
        return "success";
    }
}
