package com.arrow_academy.test_service.service;

import com.arrow_academy.test_service.dao.QuestionDao;
import com.arrow_academy.test_service.dao.StudentTestDao;
import com.arrow_academy.test_service.dao.TestDao;
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

    public ResponseEntity<String> addTest(String token, String title, String subject, String startTime, int duration, int testWindow, List<Question> questionList, List<MultipartFile> imageFiles) throws IOException, ParseException {

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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date parsedDate = sdf.parse(startTime);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            test.setStart_time(timestamp);
            test.setDuration(duration);
            test.setTestWindow(testWindow);

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
        if(jwtService.parseTokenAsJSON(token).get("role").equals("faculty") || jwtService.parseTokenAsJSON(token).get("role").equals("student")) {
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

    public ResponseEntity<?> getAllQuestionsWrappersForATest(String token, int id) {
            Test test = testDao.findById(id).get();
            List<QuestionWrapper> questionWrappers = new ArrayList<>();
            List<Integer> questionIds = test.getQuestionIds();

            for(int questionId : questionIds) {
                Question question = questionDao.findById(questionId).get();

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

    public ResponseEntity<?> submitTest(Integer id, String token, List<Response> responses) throws IOException, URISyntaxException, InterruptedException {

        int studentId = userInterface.getStudentId(token).getBody();

        StudentTest studentTest = studentTestDao.findByStudentIdAndTestId(studentId, id).get();

        if(!studentTest.isAttempted()) {
            studentTest.setStudentId(studentId);
            studentTest.setTestId(id);
            studentTest.setAttempted(true);

            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> responsesMap = new HashMap<>();
            for (Response response : responses) {
                responsesMap.put(
                        response.getQuestionId().toString(),
                        response.getOptionSelected()
                );
            }

            // Convert to JSON string
            String jsonString = mapper.writeValueAsString(responsesMap);
            studentTest.setResponses(jsonString);

            studentTestDao.save(studentTest);
            return new ResponseEntity<>("Test submitted successfully", HttpStatus.OK);
        } else return new ResponseEntity<>("You have already taken this test", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> attemptQuestionsOfATest(String token, int id) {
        try {
            int sid = Integer.parseInt(String.valueOf(userInterface.getStudentId(token).getBody()));

            Optional<StudentTest> studentTestDetails = studentTestDao.findByStudentIdAndTestId(sid, id);

            if(studentTestDetails.isEmpty()) {
                Test test = testDao.findById(id).orElseThrow(() -> new RuntimeException("Test not found"));

                Timestamp testStartTime = test.getStart_time();
                int windowSeconds = test.getTestWindow();
                int durationSeconds = test.getDuration();

                Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());

                LocalDateTime startTime = testStartTime.toLocalDateTime();
                LocalDateTime maxStartTime = startTime.plusSeconds(windowSeconds);
                LocalDateTime maxEndTime = startTime.plusSeconds(windowSeconds + durationSeconds);

                StudentTest studentTest = new StudentTest();
                studentTest.setStudentId(sid);
                studentTest.setTestId(id);

                if (currentTimestamp.before(testStartTime)) {
                    return new ResponseEntity<>("Test did not start yet.", HttpStatus.BAD_REQUEST);
                }

                if (currentTimestamp.after(Timestamp.valueOf(maxEndTime))) {
                    return new ResponseEntity<>("Test is completed.", HttpStatus.BAD_REQUEST);
                }

                // Determine actual start time
                Timestamp actualStartTime;
                Timestamp actualEndTime;
                if (currentTimestamp.before(Timestamp.valueOf(maxStartTime))) {
                    actualStartTime = currentTimestamp;
                } else {
                    actualStartTime = Timestamp.valueOf(maxStartTime);
                }
                actualEndTime = Timestamp.valueOf(actualStartTime.toLocalDateTime().plusSeconds(durationSeconds));

                studentTest.setStart_time(actualStartTime);
                studentTest.setEnd_time(actualEndTime);
                studentTest.setAttempted(false);
                studentTestDao.save(studentTest);

                return getAllQuestionsWrappersForATest(token, id);
            } else {
                if(Timestamp.valueOf(LocalDateTime.now()).before(studentTestDetails.get().getEnd_time())) {
                    if(studentTestDetails.get().isAttempted()) return new ResponseEntity<>("You have already attempted this test.", HttpStatus.BAD_REQUEST);
                    else return getAllQuestionsWrappersForATest(token, id);
                }
                else
                    return new ResponseEntity<>("Test is completed", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Error attempting test with message: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> saveTest(int id, String token, List<Response> responses) throws JsonProcessingException {
        int studentId = userInterface.getStudentId(token).getBody();

        StudentTest studentTest = studentTestDao.findByStudentIdAndTestId(studentId, id).get();

        if(!studentTest.isAttempted()) {
            studentTest.setStudentId(studentId);
            studentTest.setTestId(id);

            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> responsesMap = new HashMap<>();
            for (Response response : responses) {
                responsesMap.put(
                        response.getQuestionId().toString(),
                        response.getOptionSelected()
                );
            }

            // Convert to JSON string
            String jsonString = mapper.writeValueAsString(responsesMap);
            studentTest.setResponses(jsonString);

            studentTestDao.save(studentTest);
            return new ResponseEntity<>("Test saved successfully", HttpStatus.OK);
        } else return new ResponseEntity<>("You have already taken this test", HttpStatus.BAD_REQUEST);
    }
}
