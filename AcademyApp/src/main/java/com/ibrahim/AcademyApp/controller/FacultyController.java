package com.ibrahim.AcademyApp.controller;

import com.ibrahim.AcademyApp.model.Faculty;
import com.ibrahim.AcademyApp.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FacultyController {

    @Autowired
    private FacultyService service;

    @GetMapping("/faculties")
    public ResponseEntity<List<Faculty>> getFaculties() {
        return new ResponseEntity<>(service.findFaculties(), HttpStatus.OK);
    }

    @PostMapping("/faculty")
    public String addFaculty(@RequestBody Faculty faculty) {
        service.addFaculty(faculty);
        return "Faculty Added";
    }
}
