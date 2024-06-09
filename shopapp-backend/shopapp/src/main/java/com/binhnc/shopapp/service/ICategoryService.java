package com.binhnc.shopapp.service;


import com.binhnc.shopapp.dto.CategoryDTO;
import com.binhnc.shopapp.model.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category updateCategory(Long id, CategoryDTO category);

    void deleteCategory(Long id);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();
}
