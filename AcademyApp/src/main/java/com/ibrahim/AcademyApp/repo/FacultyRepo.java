package com.ibrahim.AcademyApp.repo;

import com.ibrahim.AcademyApp.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepo extends JpaRepository<Faculty, Integer> {
}
