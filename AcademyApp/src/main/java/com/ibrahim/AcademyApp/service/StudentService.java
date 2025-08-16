package com.ibrahim.AcademyApp.service;

import com.ibrahim.AcademyApp.model.Student;
import com.ibrahim.AcademyApp.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepo repo;
    public List<Student> getStudents() {
        return repo.findAll();
    }

    public void addStudent(Student student) {
        repo.save(student);
    }

    public Student getStudentById(int sid) {
        return repo.findById(sid).orElse(new Student());
    }

    public void updateStudent(int sid, Student student) {
        Student student1 = repo.findById(sid).orElse(new Student());

        student1.setMarks(student.getMarks());
        student1.setName(student.getName());
        student1.setStandard(student.getStandard());
        repo.save(student1);
    }

    public List<Student> getStudentsByParam(int standard) {

        return repo.findByStandard(standard);
    }
}
