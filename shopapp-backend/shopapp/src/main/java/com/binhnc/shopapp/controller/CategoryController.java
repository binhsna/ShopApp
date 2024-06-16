package com.binhnc.shopapp.controller;

import com.binhnc.shopapp.dto.CategoryDTO;
import com.binhnc.shopapp.model.Category;
import com.binhnc.shopapp.response.ListMessageResponse;
import com.binhnc.shopapp.response.MessageResponse;
import com.binhnc.shopapp.response.UpdateCategoryResponse;
import com.binhnc.shopapp.service.ICategoryService;
import com.binhnc.shopapp.component.LocalizationUtils;
import com.binhnc.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
// Dependence Inject
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;
    private final LocalizationUtils localizationUtils;

    // Thêm mới category
    @PostMapping("")
    // Nếu tham số truyền vào là 1 object thì sao? => Data Transfer Object = Request Object
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        ListMessageResponse.builder()
                                .messages(errorMessages)
                                .build()
                );
            }
            categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(MessageResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(MessageResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_FAILED))
                    .build());
        }
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
            @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(
                UpdateCategoryResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY))
                        .build()
        );
    }

    // Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                MessageResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY))
                        .build()
        );
    }
}
