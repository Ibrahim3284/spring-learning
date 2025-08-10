package com.ibrahim.SpringJDBCEx.service;

import com.ibrahim.SpringJDBCEx.model.Student;
import com.ibrahim.SpringJDBCEx.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private StudentRepo repo;

    public StudentRepo getRepo() {
        return repo;
    }

    @Autowired
    public void setRepo(StudentRepo repo) {
        this.repo = repo;
    }

    public void addStudent(Student stu) {
        repo.save(stu);
    }

    public List<Student> getStudents() {
        return repo.findAll();
    }
}
