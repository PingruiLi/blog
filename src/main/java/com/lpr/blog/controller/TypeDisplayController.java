package com.lpr.blog.controller;

import com.lpr.blog.entity.Type;
import com.lpr.blog.service.BlogService;
import com.lpr.blog.service.TypeService;
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
public class TypeDisplayController {
    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String list(@PathVariable Integer id, Model model, @PageableDefault(size = 6, sort = {"updateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        List<Type> types = typeService.getTopTypes(1000); //相当于查询全表,但是按blog数量排序
        if(id == -1) {
            id = types.get(0).getId();
        }
        BlogQuery query = new BlogQuery();
        query.setTypeId(id);
        model.addAttribute("types",types);
        model.addAttribute("page", blogService.listBlog(pageable,query));
        model.addAttribute("activeId",id); //放回去来选中active type
        return "types";
    }
}
