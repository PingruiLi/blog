package com.lpr.blog.controller.admin;

import com.lpr.blog.entity.Type;
import com.lpr.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 6,sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("types/input")
    public String input(Model model) {
        Type type = new Type();
        model.addAttribute("type", type);
        return "admin/create_type";
    }

    @PostMapping("/types")
    public String save(@Valid Type type, BindingResult result, RedirectAttributes attributes, Model model) {
        if(typeService.getTypeByName(type.getName()) != null) {
            result.rejectValue("name","nameError","该分类已存在");
        }
        System.out.println(result.getAllErrors());
        if(result.hasErrors()) {
            return "admin/create_type";
        }
        Type savedType = typeService.saveType(type);
        if(savedType == null) {
            attributes.addFlashAttribute("message","操作失败");
        } else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("types/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/create_type";
    }

    @PostMapping("types/{id}")
    public String editType(@Valid Type type, BindingResult result,@PathVariable Integer id, Model model, RedirectAttributes attributes) {
        Type edit = typeService.getTypeByName(type.getName());
        if(edit != null) {
            result.rejectValue("name", "nameError", "该分类已存在");
        }
        if(result.hasErrors()) {
            System.out.println("前面出错啦");
            return "admin/create_type";
        }
        Type editted = typeService.updateType(id, type);
        System.out.println(editted);
        if(editted == null) {
            attributes.addFlashAttribute("message","修改失败");
        } else {
            attributes.addFlashAttribute("message","修改成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }

}
