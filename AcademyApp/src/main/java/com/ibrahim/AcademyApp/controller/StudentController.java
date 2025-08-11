package com.ibrahim.AcademyApp.controller;

import com.ibrahim.AcademyApp.model.Student;
import com.ibrahim.AcademyApp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping("/students")
    @ResponseBody
    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students = service.getStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping("/student")
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        service.addStudent(student);
        return new ResponseEntity<>("Student added", HttpStatus.CREATED);
    }

    @GetMapping("/student/{studentID}")
    public ResponseEntity<Student> getStudentById(@PathVariable("studentID") int sid) {
        return new ResponseEntity<>(service.getStudentById(sid), HttpStatus.OK);
    }
}
