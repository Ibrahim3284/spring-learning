package com.arrow_academy.test_service.model;

import jakarta.persistence.*;

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
}
