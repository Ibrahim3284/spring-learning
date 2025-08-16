package com.ibrahim.ecs_demo.repo;

import com.ibrahim.ecs_demo.model.Ecs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcsRepo extends JpaRepository<Ecs, Integer> {
}
