package com.arrow_academy.test_service.service;

import com.arrow_academy.test_service.dao.QuestionDao;
import com.arrow_academy.test_service.dao.StudentTestDao;
import com.arrow_academy.test_service.dao.TestDao;
import com.arrow_academy.test_service.dao.TestDetailsDao;
import com.arrow_academy.test_service.feign.UserInterface;
import com.arrow_academy.test_service.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TestService {

    @Autowired
    private TestDao testDao;

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private StudentTestDao studentTestDao;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private TestDetailsDao testDetailsDao;

    public ResponseEntity<?> addTest(String token, String title, Date date, String subject, List<Question> questions, List<MultipartFile> images) throws IOException {
        String role = String.valueOf(jwtService.parseTokenAsJSON(token).get("role"));

        if(!role.equals("faculty")) return new ResponseEntity<>("Only faculties can add test", HttpStatus.FORBIDDEN);
        Optional<TestDetails> testDetails = testDetailsDao.findByTitleAndDate(title, date);

        if(testDetails.isEmpty()) return new ResponseEntity<>("Test details doesnt exist", HttpStatus.NOT_FOUND);

        int i = 0;
        for(Question question : questions) {
            if(question.isHasImage()) {
                question.setQuestionImageName(images.get(i).getName());
                question.setQuestionImageType(images.get(i).getContentType());
                question.setQuestionImageData(images.get(i).getBytes());

                i++;
            }
        }

        List<Question> questionsAdded = questionDao.saveAll(questions);

        Test test = new Test();
        test.setSubject(subject);
        List<Integer> questionIds = new ArrayList<>();

        for(Question question : questionsAdded) questionIds.add(question.getId());
        test.setQuestionIds(questionIds);

        TestDetails testDetails1 = testDetails.get();
        test.setTestDetails(testDetails1);

        testDao.save(test);
        return new ResponseEntity<>("Test created successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<?> getQuestions(String token, String title, Date date) {
        String role = String.valueOf(jwtService.parseTokenAsJSON(token).get("role"));

        if(!role.equals("faculty") && !role.equals("student")) return new ResponseEntity<>("Test can be viewed by students or faculties", HttpStatus.FORBIDDEN);

        if(role.equals("faculty")) {
            Optional<TestDetails> testDetails = testDetailsDao.findByTitleAndDate(title, date);
            if(testDetails.isEmpty()) return new ResponseEntity<>("Test doesnt exist", HttpStatus.NOT_FOUND);

            Map<String, List<Question>> questionsSubjectMap = new HashMap<>();

            int testDetailsId = testDetails.get().getId();
            List<Test> tests = testDao.findByTestDetailsId(testDetailsId);

            for(Test test :  tests) {
                List<Integer> questionIds = test.getQuestionIds();
                List<Question> questions = new ArrayList<>();

                for(Integer questionId : questionIds) questions.add(questionDao.findById(questionId).get());

                questionsSubjectMap.put(test.getSubject(), questions);
            }

            return new ResponseEntity<>(questionsSubjectMap, HttpStatus.OK);
        } else {
            Optional<TestDetails> testDetails = testDetailsDao.findByTitleAndDate(title, date);
            if(testDetails.isEmpty()) return new ResponseEntity<>("Test doesnt exist", HttpStatus.NOT_FOUND);

            Map<String, List<QuestionWrapper>> questionsSubjectMap = new HashMap<>();

            int testDetailsId = testDetails.get().getId();
            List<Test> tests = testDao.findByTestDetailsId(testDetailsId);

            for(Test test :  tests) {
                List<Integer> questionIds = test.getQuestionIds();
                List<QuestionWrapper> questionWrappers = new ArrayList<>();

                for(Integer questionId : questionIds) {
                    Question question = questionDao.findById(questionId).get();

                    QuestionWrapper questionWrapper = new QuestionWrapper();
                    questionWrapper.setQuestionTitle(question.getQuestionTitle());
                    questionWrapper.setQuestion(question.getQuestionImageData());
                    questionWrapper.setId(questionId);
                    questionWrapper.setOption1(question.getOption1());
                    questionWrapper.setOption2(question.getOption2());
                    questionWrapper.setOption3(question.getOption3());
                    questionWrapper.setOption4(question.getOption4());

                    questionWrappers.add(questionWrapper);
                }

                questionsSubjectMap.put(test.getSubject(), questionWrappers);
            }

            return new ResponseEntity<>(questionsSubjectMap, HttpStatus.OK);
        }
    }

    public ResponseEntity<?> attemptTest(String token, String title, Date date) {
        try {
            String role = String.valueOf(jwtService.parseTokenAsJSON(token).get("role"));

            if(!role.equals("student")) return new ResponseEntity<>("Only students can take up this test", HttpStatus.FORBIDDEN);

            Optional<TestDetails> testDetails = testDetailsDao.findByTitleAndDate(title, date);
            if(testDetails.isEmpty()) return new ResponseEntity<>("Test doesnt exist", HttpStatus.NOT_FOUND);

            int studentId = Integer.parseInt(String.valueOf(userInterface.getStudentId(token).getBody()));

            TestDetails testDetails1 = testDetails.get();
            Optional<StudentTest> studentTestDetails = studentTestDao.findByStudentIdAndTestDetailsId(studentId, testDetails1.getId());

            if(studentTestDetails.isEmpty()) {

                Timestamp testStartTime = testDetails1.getStartTime();
                int window = testDetails1.getTestWindow();
                int duration = testDetails1.getDuration();

                Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());

                LocalDateTime startTime = testStartTime.toLocalDateTime();
                LocalDateTime maxStartTime = startTime.plusMinutes(window);
                LocalDateTime maxEndTime = startTime.plusMinutes(window + duration);

                StudentTest studentTest = new StudentTest();
                studentTest.setStudentId(studentId);
                studentTest.setTestDetailsId(testDetails1.getId());

                if (currentTimestamp.before(testStartTime)) {
                    return new ResponseEntity<>("Test did not start yet.", HttpStatus.BAD_REQUEST);
                }

                if (currentTimestamp.after(Timestamp.valueOf(maxEndTime))) {
                    return new ResponseEntity<>("Test is completed.", HttpStatus.BAD_REQUEST);
                }

                Timestamp actualStartTime;
                Timestamp actualEndTime;
                if (currentTimestamp.before(Timestamp.valueOf(maxStartTime))) {
                    actualStartTime = currentTimestamp;
                } else {
                    actualStartTime = Timestamp.valueOf(maxStartTime);
                }
                actualEndTime = Timestamp.valueOf(actualStartTime.toLocalDateTime().plusMinutes(duration));

                studentTest.setStart_time(actualStartTime);
                studentTest.setEnd_time(actualEndTime);
                studentTest.setAttempted(false);
                studentTestDao.save(studentTest);

                return getQuestions(token, title, date);
            } else {
                if(Timestamp.valueOf(LocalDateTime.now()).before(studentTestDetails.get().getEnd_time())) {
                    if(studentTestDetails.get().isAttempted()) return new ResponseEntity<>("You have already attempted this test.", HttpStatus.BAD_REQUEST);
                    else return getQuestions(token, title, date);
                }
                else
                    return new ResponseEntity<>("Test is completed", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Error attempting test with message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> saveTest(String token, String title, Date date, List<Response> responses) throws JsonProcessingException {
        String role = String.valueOf(jwtService.parseTokenAsJSON(token).get("role"));

        if(!role.equals("student")) return new ResponseEntity<>("Only students can take up this test", HttpStatus.FORBIDDEN);

        Optional<TestDetails> testDetails = testDetailsDao.findByTitleAndDate(title, date);
        if(testDetails.isEmpty()) return new ResponseEntity<>("Test doesnt exist", HttpStatus.NOT_FOUND);

        TestDetails testDetails1 = testDetails.get();
        int studentId = Integer.parseInt(String.valueOf(userInterface.getStudentId(token).getBody()));

        StudentTest studentTest = studentTestDao.findByStudentIdAndTestDetailsId(studentId, testDetails1.getId()).get();

        if(!studentTest.isAttempted()) {
            studentTest.setStudentId(studentId);
            studentTest.setTestDetailsId(testDetails1.getId());

            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> responsesMap = new HashMap<>();
            for (Response response : responses) {
                responsesMap.put(
                        response.getQuestionId().toString(),
                        response.getOptionSelected()
                );
            }

            String jsonString = mapper.writeValueAsString(responsesMap);
            studentTest.setResponses(jsonString);

            studentTestDao.save(studentTest);
            return new ResponseEntity<>("Test saved successfully", HttpStatus.OK);
        } else return new ResponseEntity<>("You have already taken this test", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> submitTest(String token, String title, Date date, List<Response> responses) throws JsonProcessingException {
        String role = String.valueOf(jwtService.parseTokenAsJSON(token).get("role"));

        if(!role.equals("student")) return new ResponseEntity<>("Only students can take up this test", HttpStatus.FORBIDDEN);

        Optional<TestDetails> testDetails = testDetailsDao.findByTitleAndDate(title, date);
        if(testDetails.isEmpty()) return new ResponseEntity<>("Test doesnt exist", HttpStatus.NOT_FOUND);

        TestDetails testDetails1 = testDetails.get();

        int studentId = userInterface.getStudentId(token).getBody();

        StudentTest studentTest = studentTestDao.findByStudentIdAndTestDetailsId(studentId, testDetails1.getId()).get();

        if(!studentTest.isAttempted()) {
            studentTest.setStudentId(studentId);
            studentTest.setTestDetailsId(testDetails1.getId());
            studentTest.setAttempted(true);

            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> responsesMap = new HashMap<>();
            for (Response response : responses) {
                responsesMap.put(
                        response.getQuestionId().toString(),
                        response.getOptionSelected()
                );
            }

            String jsonString = mapper.writeValueAsString(responsesMap);
            studentTest.setResponses(jsonString);

            studentTestDao.save(studentTest);
            return new ResponseEntity<>("Test submitted successfully", HttpStatus.OK);
        } else return new ResponseEntity<>("You have already taken this test", HttpStatus.BAD_REQUEST);
    }
}
