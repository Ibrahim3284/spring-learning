package com.ibrahim.quiz_service.service;

import com.ibrahim.quiz_service.feign.QuizInterface;
import com.ibrahim.quiz_service.model.QuestionWrapper;
import com.ibrahim.quiz_service.model.Quiz;
import com.ibrahim.quiz_service.model.Response;
import com.ibrahim.quiz_service.repo.QuizDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);

        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();

        List<QuestionWrapper> questionWrapperList = quizInterface.getQuestionsFromID(questionIds).getBody();


        return new ResponseEntity<>(questionWrapperList, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> response) {

        return new ResponseEntity<>(quizInterface.getScore(response).getBody(), HttpStatus.OK);
    }
}
