package com.lpr.blog.controller;

import com.lpr.blog.entity.Blog;
import com.lpr.blog.entity.Comment;
import com.lpr.blog.entity.User;
import com.lpr.blog.service.BlogService;
import com.lpr.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}") //取到yml文件里定义的值
    private String avatarPath;

    @GetMapping("/comments/{blogId}")
    public String comment(@PathVariable Integer blogId, Model model){
        model.addAttribute("comments", commentService.listCommentByBlog(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String postComment(Comment comment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Blog blog = blogService.getBlog(comment.getBlog().getId());
        if(user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdmin(true);
        } else {
            comment.setAvatar(avatarPath);
        }
        comment.setBlog(blog);
        commentService.postComment(comment);
        return "redirect:/comments/" + comment.getBlog().getId();
    }


}
