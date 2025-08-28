package com.arrowacademy.user_service.dao;

import com.arrowacademy.user_service.model.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FacultyDao extends JpaRepository<Faculty, Integer> {
    @Query("SELECT f FROM Faculty f")
    Page<Faculty> findByPageParams(Pageable pageable);

    Optional<Faculty> findByEmail(String email);
}
