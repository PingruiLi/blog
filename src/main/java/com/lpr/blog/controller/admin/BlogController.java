package com.lpr.blog.controller.admin;

import com.lpr.blog.entity.Blog;
import com.lpr.blog.entity.Tag;
import com.lpr.blog.entity.Type;
import com.lpr.blog.entity.User;
import com.lpr.blog.service.BlogService;
import com.lpr.blog.service.TagService;
import com.lpr.blog.service.TypeService;
import com.lpr.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String list(@PageableDefault(size=6,sort={"updateDate"},direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        model.addAttribute("types",typeService.listType());
        return "admin/administration";
    }

    @PostMapping("/blogs/search") //只刷新下半部分的表格，局部渲染
    public String search(@PageableDefault(size=2,sort={"updateDate"},direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/administration::blogList";
    }

    @GetMapping("/blogs/post")
    public String post(Model model){
        model.addAttribute("blog", new Blog());
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        return "admin/post";
    }

    @PostMapping("/blogs/post")
    public String publish(Model model, Blog blog, HttpSession session, RedirectAttributes attributes){
        blog.setUser((User) session.getAttribute("user"));
        Type type = typeService.getType(blog.getType().getId());
        List<Tag> tags = tagService.getTags(blog.getTagIds());
        blog.setTags(tags);
        blog.setType(type);
        Blog savedBlog;
        Integer id = blog.getId();
        if(id == null) {
            savedBlog = blogService.saveBlog(blog);
        } else {
            savedBlog = blogService.updateBlog(id, blog);
        }
        if(savedBlog == null) {
            attributes.addFlashAttribute("messgae","发布失败");
        } else {
            attributes.addFlashAttribute("message","发布成功");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/edit")
    public String edit(Model model, @PathVariable Integer id) {
        Blog blog = blogService.getBlog(id);
        blog.initTagIds();
        model.addAttribute("blog", blog);
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        return "admin/post";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";
    }
}
