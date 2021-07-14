package com.lpr.blog.controller;

import com.lpr.blog.service.BlogService;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archive(Model model){
        model.addAttribute("map",blogService.archiveBlogs());
        model.addAttribute("count",blogService.countBlogs());
        return "archives";
    }
}
