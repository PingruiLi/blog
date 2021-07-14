package com.lpr.blog.controller;

import com.lpr.blog.entity.Blog;
import com.lpr.blog.entity.Tag;
import com.lpr.blog.service.BlogService;
import com.lpr.blog.service.TagService;
import com.lpr.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagDisplayController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String list(@PathVariable Integer id, Model model,
                       @PageableDefault(size = 6, sort = {"updateDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        List<Tag> tags = tagService.getTopTags(1000);
        model.addAttribute("tags", tags);
        if(id == -1) {
            id = tags.get(0).getId();
        }
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("tags",tags);
        model.addAttribute("activeId",id);
        return "tags";
    }
}
