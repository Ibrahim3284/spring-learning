package com.arrow_academy.test_service.dao;

import com.arrow_academy.test_service.model.StudentTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentTestDao extends JpaRepository<StudentTest, Integer> {

}
