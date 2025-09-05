package com.arrow_academy.test_service.dao;

import com.arrow_academy.test_service.model.StudentTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentTestDao extends JpaRepository<StudentTest, Integer> {

    Optional<StudentTest> findByStudentIdAndTestDetailsId(Integer sid, Integer tid);
}
