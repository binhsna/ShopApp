package com.binhnc.shopapp.controller;

import com.binhnc.shopapp.dto.CategoryDTO;
import com.binhnc.shopapp.model.Category;
import com.binhnc.shopapp.response.UpdateCategoryResponse;
import com.binhnc.shopapp.service.ICategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
// Dependence Inject
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    // Thêm mới category
    @PostMapping("")
    // Nếu tham số truyền vào là 1 object thì sao? => Data Transfer Object = Request Object
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert category successfully");
    }

    // Hiển thị tất cả các categories
    // http://localhost:8080/api/v1/categories?page=1&limit=10
    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }


    // Update category
    @PutMapping("/{id}")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO,
            HttpServletRequest request) {
        categoryService.updateCategory(id, categoryDTO);
        Locale locale = localeResolver.resolveLocale(request);
        return ResponseEntity.ok(
                UpdateCategoryResponse.builder()
                        .message(messageSource.getMessage("category.update.update_successfully", null, locale))
                        .build()
        );
    }

    // Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category with id: " + id + " successfully");
    }
}
