package com.lite.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lite.entity.Category;
import com.lite.mapper.CategoryMapper;
import com.lite.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
