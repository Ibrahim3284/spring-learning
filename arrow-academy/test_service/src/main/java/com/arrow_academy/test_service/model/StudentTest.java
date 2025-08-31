package com.arrow_academy.test_service.model;

import jakarta.persistence.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

@Entity
public class StudentTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int studentId;
    private int testId;
    private int start_time;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String responses;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public String getResponses() {
        return responses;
    }

    public void setResponses(String responses) {
        this.responses = responses;
    }
}
