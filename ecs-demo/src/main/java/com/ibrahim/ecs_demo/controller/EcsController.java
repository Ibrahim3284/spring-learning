package com.ibrahim.ecs_demo.controller;

import com.ibrahim.ecs_demo.model.Ecs;
import com.ibrahim.ecs_demo.service.EcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EcsController {

    @Autowired
    EcsService service;

    @GetMapping("/ecss")
    public List<Ecs> getEcss() {
        return service.getEcss();
    }

    @PostMapping("/ecs")
    public String addEcs(@RequestBody Ecs ecs) {
        service.addEcs(ecs);

        return "Ecs added";
    }
}
