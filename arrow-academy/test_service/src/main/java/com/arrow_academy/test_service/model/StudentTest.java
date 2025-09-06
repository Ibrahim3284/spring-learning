package com.arrow_academy.test_service.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class StudentTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int studentId;
    private int testDetailsId;
    private Instant start_time;
    private Instant end_time;

    private boolean isAttempted;

    @Column(columnDefinition = "TEXT")
    private String responses;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTestDetailsId() {
        return testDetailsId;
    }

    public void setTestDetailsId(int testDetailsId) {
        this.testDetailsId = testDetailsId;
    }

    public Instant getStart_time() {
        return start_time;
    }

    public void setStart_time(Instant start_time) {
        this.start_time = start_time;
    }

    public String getResponses() {
        return responses;
    }

    public void setResponses(String responses) {
        this.responses = responses;
    }

    public Instant getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Instant end_time) {
        this.end_time = end_time;
    }

    public boolean isAttempted() {
        return isAttempted;
    }

    public void setAttempted(boolean attempted) {
        isAttempted = attempted;
    }
}
