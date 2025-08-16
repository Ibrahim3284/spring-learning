package com.ibrahim.ecs_demo.service;

import com.ibrahim.ecs_demo.model.Ecs;
import com.ibrahim.ecs_demo.repo.EcsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EcsService {

    @Autowired
    EcsRepo repo;

    public List<Ecs> getEcss() {

        return repo.findAll();
    }

    public void addEcs(Ecs ecs) {
        repo.save(ecs);
    }
}
