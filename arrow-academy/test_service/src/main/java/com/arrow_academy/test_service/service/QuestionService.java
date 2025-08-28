package com.arrow_academy.test_service.service;

import com.arrow_academy.test_service.dao.QuestionDao;
import com.arrow_academy.test_service.model.Question;
import com.arrow_academy.test_service.model.QuestionWrapper;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    public ResponseEntity<String> addQuestion(Question question, MultipartFile imageFile) throws IOException {
        question.setQuestionImageName(imageFile.getOriginalFilename());
        question.setQuestionImageType(imageFile.getContentType());
        question.setQuestionImageData(imageFile.getBytes());

        questionDao.save(question);
        return new ResponseEntity<>("Question added", HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getAllQuestions() {
        List<QuestionWrapper> questionWrappers = new ArrayList<>();
        List<Question> questions = questionDao.findAll();

        for(Question question : questions) {
            QuestionWrapper questionWrapper = new QuestionWrapper();
            questionWrapper.setId(question.getId());
            questionWrapper.setQuestion(question.getQuestionImageData());
            questionWrapper.setOption1(question.getOption1());
            questionWrapper.setOption2(question.getOption2());
            questionWrapper.setOption3(question.getOption3());
            questionWrapper.setOption4(question.getOption4());
            questionWrappers.add(questionWrapper);
        }
        return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }

    public ResponseEntity<List<Question>> getAllQuestionsRaw() {

        return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<String> addMultipleQuestions(List<Question> questions, List<MultipartFile> imageFiles) throws IOException {
        System.out.println("Size: " + questions.size());
        System.out.println("Image files: " + imageFiles.size());
        if(questions.size() != imageFiles.size()) return new ResponseEntity<>("Number of questions are incorrect.", HttpStatus.BAD_REQUEST);
        else {
            int i = 0;
            for(Question question : questions) {
                question.setQuestionImageName(imageFiles.get(i).getOriginalFilename());
                question.setQuestionImageType(imageFiles.get(i).getContentType());
                question.setQuestionImageData(imageFiles.get(i).getBytes());

                questionDao.save(question);
                i++;
            }
        }
        return new ResponseEntity<>("Added Multiple questions", HttpStatus.CREATED);
    }
}
