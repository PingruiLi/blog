package com.lpr.blog.controller;

import com.lpr.blog.entity.Blog;
import com.lpr.blog.service.BlogService;
import com.lpr.blog.service.TagService;
import com.lpr.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @RequestMapping("/")
    public String index(@PageableDefault(size = 8,sort = {"updateDate"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.getTopTypes(2));
        model.addAttribute("tags",tagService.getTopTags(2));
        model.addAttribute("recommendedBlogs",blogService.getRecommendedBlogs(2));
        return "index";
    }

    @GetMapping("/blogs/{id}")
    public String blog(@PathVariable Integer id, Model model){
        model.addAttribute("blog",blogService.getAndUpdateView(id));
        return "blog";
    }

    @RequestMapping("/search")
    //@PostMapping("/search")
    public String search(Model model, @PageableDefault(size = 8, sort={"updateDate"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String key) {
        Page<Blog> page = blogService.searchBlog(pageable, "%" + key + "%");
        model.addAttribute("key",key);
        model.addAttribute("page",page);
        return "search";
    }

    @GetMapping("/aboutme")
    public String aboutMe(){
        return "aboutMe";
    }

    @GetMapping("/footer")
    public String footer(Model model) {
        List<Blog> newBlogs = blogService.getRecommendedBlogs(3);
        model.addAttribute("newBlogs", newBlogs);
        System.out.println("\n\n\ncalled\n\n\n");
        return "_fragment :: newBlog";
    }
}
