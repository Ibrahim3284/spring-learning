package com.arrow_academy.test_service.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;

@Entity
@Table (
        name = "test_details",
        uniqueConstraints = @UniqueConstraint(columnNames = {"title", "date"})
)
public class TestDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private int testWindow;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTestWindow() {
        return testWindow;
    }

    public void setTestWindow(int testWindow) {
        this.testWindow = testWindow;
    }
}
