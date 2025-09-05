package com.arrow_academy.test_service.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String subject;
    @ElementCollection
    private List<Integer> questionIds;

    @ManyToOne
    @JoinColumn(name = "test_details_id") // foreign key column in the "test" table
    private TestDetails testDetails;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Integer> questionIds) {
        this.questionIds = questionIds;
    }

    public TestDetails getTestDetails() {
        return testDetails;
    }

    public void setTestDetails(TestDetails testDetails) {
        this.testDetails = testDetails;
    }
}
