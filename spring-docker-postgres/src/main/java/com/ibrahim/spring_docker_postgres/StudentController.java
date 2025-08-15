package com.ibrahim.spring_docker_postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    StudentRepo repo;

    @GetMapping("/students")
    public List<Student> getStudents() {
        return repo.findAll();
    }

    @RequestMapping("/addStudent")
    public void addStudent() {

        Student s = new Student();
        s.setName("Raj");
        s.setAge(23);

        repo.save(s);
    }
}
