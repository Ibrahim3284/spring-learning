package com.ibrahim.AcademyApp.repo;

import com.ibrahim.AcademyApp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, Integer> {
    List<Student> findByStandard(int standard);
}
