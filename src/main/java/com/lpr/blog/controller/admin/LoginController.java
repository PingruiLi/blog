package com.lpr.blog.controller.admin;

import com.lpr.blog.entity.User;
import com.lpr.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, RedirectAttributes attributes, Model model) {
        User user = userService.validate(username, password);
        if(user != null) {
            user.setPassword(null);
            model.addAttribute("username",user.getUsername());
            session.setAttribute("user", user);
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/admin"; //return的是静态页面，redirect到的是url，分清楚
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    @GetMapping("/post")
    public String post(){
        return "admin/post";
    }
}
