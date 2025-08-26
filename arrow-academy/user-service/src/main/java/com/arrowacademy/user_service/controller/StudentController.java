package com.arrowacademy.user_service.controller;

import com.arrowacademy.user_service.model.Student;
import com.arrowacademy.user_service.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("add")
    public ResponseEntity<?> addStudent(@RequestHeader("Authorization") String token, @Valid @RequestBody Student student, BindingResult result) {
        if (result.hasErrors()) {
            String firstError = result.getFieldErrors()
                    .stream()
                    .findFirst()
                    .map(error -> error.getDefaultMessage()) // ✅ only the exact message
                    .orElse("Invalid input");

            return new ResponseEntity<>(firstError, HttpStatus.BAD_REQUEST);
        }

        return studentService.addStudent(token, student);
    }

    @PutMapping("/{id}/inactive")
    public ResponseEntity<String> makeStudentInactive(@RequestHeader("Authorization") String token, @PathVariable("id") Integer id) {
        return studentService.makeStudentInactive(token, id);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateStudent(@RequestHeader("Authorization") String token, @PathVariable("id") Integer id, @Valid @RequestBody Student student, BindingResult result) {
        if (result.hasErrors()) {
            // Collect all error messages
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return studentService.updateStudent(token, id, student);
    }

    @GetMapping("allStudents")
    public ResponseEntity<Page<Student>> getAllStudents(@RequestHeader("Authorization") String token, @RequestParam(value = "page_size", required = false) Integer pageSize, @RequestParam(value = "page_no", required = false) Integer pageNo) {
        if(pageSize != null && pageNo != null)
            return studentService.getAllStudents(token, pageSize, pageNo);
        else if(pageSize == null && pageNo == null)
            return studentService.getAllStudents(token,20,1);
        else if(pageSize == null)
            return studentService.getAllStudents(token,20, pageNo);
        else return studentService.getAllStudents(token,pageSize, 1);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentById(@RequestHeader("Authorization") String token, @PathVariable("id") Integer id) {
        return studentService.getStudentById(token, id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteStudentById(@RequestHeader("Authorization") String token, @PathVariable("id") Integer id) {
        return studentService.deleteStudentById(token, id);
    }
}
