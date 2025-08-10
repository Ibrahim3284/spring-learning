package com.ibrahim.JobApp;

import com.ibrahim.JobApp.model.JobPost;
import com.ibrahim.JobApp.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class JobController {

    @Autowired
    private JobService service;

    @RequestMapping({"/", "home"})
    public String home() {
        return "home";
    }

    @RequestMapping("addjob")
    public String addJob() {
        return "addjob";
    }

    @PostMapping("handleForm")
    public String handleForm(JobPost jobPost) {
        service.addJob(jobPost);
        return "success";
    }

    @RequestMapping("viewalljobs")
    public String viewAllJobs(Model model) {
        List<JobPost> jobs = service.getAllJobs();

        model.addAttribute("jobPosts", jobs);
        return "viewalljobs";
    }
}
