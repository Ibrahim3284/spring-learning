package com.arrow_academy.test_service.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table (
        name = "test",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"testTitle", "subjectName"})
        }
)
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String testTitle;
    private String subjectName;
    private Timestamp start_time;
    private int duration;
    private int window;
    @ElementCollection
    private List<Integer> questionIds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }

    public List<Integer> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Integer> questionIds) {
        this.questionIds = questionIds;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWindow() {
        return window;
    }

    public void setWindow(int window) {
        this.window = window;
    }
}
