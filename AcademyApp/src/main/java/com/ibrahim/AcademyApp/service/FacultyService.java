package com.ibrahim.AcademyApp.service;

import com.ibrahim.AcademyApp.model.Faculty;
import com.ibrahim.AcademyApp.repo.FacultyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepo repo;

    public List<Faculty> findFaculties() {
        return repo.findAll();
    }

    public void addFaculty(Faculty faculty) {
        repo.save(faculty);
    }
}
