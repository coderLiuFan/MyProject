package com.lite.controller;

import com.lite.common.R;
import com.lite.entity.Category;
import com.lite.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询当前板块列表
     * 用于首页动态展示当前所有板块
     * @return 板块实体类集合
     */
    @GetMapping(value = "/list")
    public R<List<Category>> getAll() {
        List<Category> categoryList = categoryService.list(null);
        return R.success(categoryList);
    }

}
