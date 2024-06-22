package com.binhnc.shopapp.services.category;


import com.binhnc.shopapp.dtos.CategoryDTO;
import com.binhnc.shopapp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category updateCategory(Long id, CategoryDTO category);

    void deleteCategory(Long id);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();
}
