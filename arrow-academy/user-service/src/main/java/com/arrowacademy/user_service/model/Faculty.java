package com.arrowacademy.user_service.model;

import com.arrowacademy.user_service.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(
        name = "faculty",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email", "department"})
        }
)
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @NotBlank
    @Email
    @JsonProperty("email")
    private String email;
    @NotBlank
    @JsonProperty("phoneNo")
    private String phoneNo;
    @NotBlank
    @JsonProperty("qualification")
    private String qualification;
    @NotBlank
    @JsonProperty("department")
    private String department;
    @NotNull
    @JsonProperty("dateOfBirth")
    private LocalDate dateOfBirth;
    @NotNull
    @JsonProperty("dateOfJoining")
    private LocalDate dateOfJoining;
    @NotNull
    @JsonProperty("gender")
    private Gender gender;
    @NotNull
    @JsonProperty("isActive")
    private Boolean isActive;
    @NotNull
    @JsonProperty("linkedInProfileURL")
    private String linkedInProfileURL;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getLinkedInProfileURL() {
        return linkedInProfileURL;
    }

    public void setLinkedInProfileURL(String linkedInProfileURL) {
        this.linkedInProfileURL = linkedInProfileURL;
    }
}
