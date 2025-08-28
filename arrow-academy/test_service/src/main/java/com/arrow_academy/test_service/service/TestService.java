package com.arrow_academy.test_service.service;

import com.arrow_academy.test_service.dao.QuestionDao;
import com.arrow_academy.test_service.dao.TestDao;
import com.arrow_academy.test_service.model.Question;
import com.arrow_academy.test_service.model.QuestionWrapper;
import com.arrow_academy.test_service.model.Test;
import com.arrow_academy.test_service.model.TestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestDao testDao;

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private JwtService jwtService;

    public ResponseEntity<String> addTest(String token, String title, String subject, List<Question> questionList, List<MultipartFile> imageFiles) throws IOException {

        if(jwtService.parseTokenAsJSON(token).get("role").equals("faculty")) {
            int i = 0;
            for (Question question : questionList) {
                question.setQuestionImageName(imageFiles.get(i).getOriginalFilename());
                question.setQuestionImageType(imageFiles.get(i).getContentType());
                question.setQuestionImageData(imageFiles.get(i).getBytes());

                i++;
            }
            List<Question> questionsAdded = questionDao.saveAll(questionList);

            Test test = new Test();
            test.setSubjectName(subject);
            test.setTestTitle(title);

            List<Integer> questionIds = new ArrayList<>();
            for (Question question : questionsAdded) {
                questionIds.add(question.getId());
            }
            test.setQuestionIds(questionIds);

            testDao.save(test);

            return new ResponseEntity<>("Test added successfully", HttpStatus.OK);
        } else return new ResponseEntity<>("Test can be added by faculty", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> getTest(String token, String title, String subject) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("faculty") || jwtService.parseTokenAsJSON(token).get("role").equals("student")) {

            Test test = testDao.findByTestTitleAndSubjectName(title, subject);
            List<Integer> questionIds = test.getQuestionIds();
            List<QuestionWrapper> questionWrappers = new ArrayList<>();

            for (int questionId : questionIds) {
                QuestionWrapper questionWrapper = new QuestionWrapper();
                Question question = questionDao.findById(questionId).get();

                questionWrapper.setId(question.getId());
                questionWrapper.setQuestion(question.getQuestionImageData());
                questionWrapper.setOption1(question.getOption1());
                questionWrapper.setOption2(question.getOption2());
                questionWrapper.setOption3(question.getOption3());
                questionWrapper.setOption4(question.getOption4());

                questionWrappers.add(questionWrapper);
            }
            return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
        } else return new ResponseEntity<>("Questions can be seen by students and faculties", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> getAllTests(String token) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("faculty")) {
            List<Test> tests = testDao.findAll();
            List<TestWrapper> testWrappers = new ArrayList<>();

            for (Test test : tests) {
                TestWrapper testWrapper = new TestWrapper();
                testWrapper.setTestId(test.getId());
                testWrapper.setTestTitle(test.getTestTitle());
                testWrapper.setSubjectName(test.getSubjectName());

                testWrappers.add(testWrapper);
            }

            return new ResponseEntity<>(testWrappers, HttpStatus.OK);
        } else return new ResponseEntity<>("All tests can be seen by faculties", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> getAllQuestionsForATest(String token, int id) {
        if(jwtService.parseTokenAsJSON(token).get("role").equals("faculty")) {

            Test test = testDao.findById(id).get();
            List<Integer> questionIds = test.getQuestionIds();

            List<Question> questions = new ArrayList<>();
            for (int questionId : questionIds) {
                questions.add(questionDao.findById(questionId).get());
            }

            return new ResponseEntity<>(questions, HttpStatus.OK);
        } else return new ResponseEntity<>("Test can be seen by faculties", HttpStatus.UNAUTHORIZED);
    }
}
