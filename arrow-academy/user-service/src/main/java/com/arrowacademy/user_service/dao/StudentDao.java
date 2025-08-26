package com.arrowacademy.user_service.dao;

import com.arrowacademy.user_service.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentDao extends JpaRepository<Student, Integer> {

    @Query("SELECT s FROM Student s")
    Page<Student> findByPageParams(Pageable pageable);

    Optional<Student> findByEmail(String email);
}
