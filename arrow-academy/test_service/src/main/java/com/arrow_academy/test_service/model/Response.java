package com.arrow_academy.test_service.model;

public class Response {

    private String questionId;
    private String optionSelected;

    public Response(String questionId, String optionSelected) {
        this.questionId = questionId;
        this.optionSelected = optionSelected;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getOptionSelected() {
        return optionSelected;
    }

    public void setOptionSelected(String optionSelected) {
        this.optionSelected = optionSelected;
    }
}
