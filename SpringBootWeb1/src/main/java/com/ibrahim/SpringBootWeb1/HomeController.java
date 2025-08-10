package com.ibrahim.SpringBootWeb1;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        System.out.println("Home method called");
        return "index";
    }

    @ModelAttribute("course")
    public String courseName() {
        return "Java";
    }

    // Servlet way
//    @RequestMapping("add")
//    public String add(HttpServletRequest req, HttpSession session) {
//
//        int num1 = Integer.parseInt(req.getParameter("num1"));
//        int num2 = Integer.parseInt(req.getParameter("num2"));
//
//        int result = num1 + num2;
//
//        session.setAttribute("result", result);
//        return "result.jsp";
//    }

    // Same variable name
//    @RequestMapping("add")
//    public String add(int num1, int num2, HttpSession session) {
//
//        int result = num1 + num2;
//
//        session.setAttribute("result", result);
//        return "result.jsp";
//    }

//    @RequestMapping("add")
//    public String add(@RequestParam("num1") int num3, int num2, HttpSession session) {
//
//        int result = num3 + num2;
//
//        session.setAttribute("result", result);
//        return "result.jsp";
//    }

    //Using Model
//    @RequestMapping("add")
//    public String add(@RequestParam("num1") int num3, int num2, Model model) {
//
//        int result = num3 + num2;
//
//        model.addAttribute("result", result);
//        return "result";
//    }

    // Need for ModelAttribute
//    @RequestMapping("addAlien")
//    public ModelAndView addAlien(@RequestParam("aid") int aid, @RequestParam("aname") String aname, ModelAndView mv) {
//
//        Alien alien = new Alien();
//        alien.setAid(aid);
//        alien.setAname(aname);
//        mv.addObject("alien", alien);
//        mv.setViewName("result");
//        return mv;
//    }

//    @RequestMapping("addAlien")
//    public String addAlien(@ModelAttribute("alien1") Alien alien) {
//        return "result";
//    }

    @RequestMapping("addAlien")
    public String addAlien(Alien alien) {
        return "result";
    }
}
