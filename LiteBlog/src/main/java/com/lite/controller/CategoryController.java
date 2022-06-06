package com.lite.controller;

import com.lite.common.R;
import com.lite.entity.Category;
import com.lite.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/list")
    public R<List<Category>> getAll() {
        List<Category> categoryList = categoryService.list(null);
        return R.success(categoryList);
    }


}
