package com.arrowacademy.user_service.controller;

import com.arrowacademy.user_service.feign.AuthInterface;
import com.arrowacademy.user_service.model.Faculty;
import com.arrowacademy.user_service.model.Student;
import com.arrowacademy.user_service.model.User;
import com.arrowacademy.user_service.service.FacultyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @PostMapping("addMultiple")
    public ResponseEntity<?> addMultipleFaculties(@RequestHeader("Authorization") String token, @Valid @RequestBody List<Faculty> faculties, BindingResult result) {
        if (result.hasErrors()) {
            String firstError = result.getFieldErrors()
                    .stream()
                    .findFirst()
                    .map(error -> error.getDefaultMessage()) // ✅ only the exact message
                    .orElse("Invalid input");

            return new ResponseEntity<>(firstError, HttpStatus.BAD_REQUEST);
        }

        return facultyService.addMultipleFaculties(token, faculties);
    }

    @PostMapping("add")
    public ResponseEntity<?> addFaculty(@RequestHeader("Authorization") String token, @Valid @RequestBody Faculty faculty, BindingResult result) {
        if (result.hasErrors()) {
            String firstError = result.getFieldErrors()
                    .stream()
                    .findFirst()
                    .map(error -> error.getDefaultMessage()) // ✅ only the exact message
                    .orElse("Invalid input");

            return new ResponseEntity<>(firstError, HttpStatus.BAD_REQUEST);
        }

        return facultyService.addFaculty(token, faculty);
    }

    @PutMapping("/{id}/inactive")
    public ResponseEntity<String> makeFacultyInactive(@RequestHeader("Authorization") String token, @PathVariable("id") Integer id) {
        return facultyService.makeFacultyInactive(token, id);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateFaculty(@RequestHeader("Authorization") String token, @PathVariable("id") Integer id, @Valid @RequestBody Faculty faculty, BindingResult result) {
        if (result.hasErrors()) {
            // Collect all error messages
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return facultyService.updateFaculty(token, id, faculty);
    }

    @GetMapping("allFaculties")
    public ResponseEntity<Page<Faculty>> getAllFaculties(@RequestHeader("Authorization") String token, @RequestParam(value = "page_size", required = false) Integer pageSize, @RequestParam(value = "page_no", required = false) Integer pageNo) {
        if(pageSize != null && pageNo != null)
            return facultyService.getAllFaculties(token, pageSize, pageNo);
        else if(pageSize == null && pageNo == null)
            return facultyService.getAllFaculties(token,20,1);
        else if(pageSize == null)
            return facultyService.getAllFaculties(token,20, pageNo);
        else return facultyService.getAllFaculties(token,pageSize, 1);
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyById(@RequestHeader("Authorization") String token, @PathVariable("id") Integer id) {
        return facultyService.getFacultyById(token, id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteFacultyById(@RequestHeader("Authorization") String token, @PathVariable("id") Integer id) {
        return facultyService.deleteFacultyById(token, id);
    }

    @GetMapping("getFacultyDetails")
    public ResponseEntity<?> getFacultyDetails(@RequestHeader("Authorization") String token) {
        return facultyService.getFacultyDetails(token);
    }
}
