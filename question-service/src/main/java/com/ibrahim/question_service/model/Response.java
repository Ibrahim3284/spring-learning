package com.ibrahim.question_service.model;

public class Response {

    int id;
    private String response;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Response(int id, String response) {
        this.id = id;
        this.response = response;
    }
}
