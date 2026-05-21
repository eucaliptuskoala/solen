package org.solen.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.solen.business.categorycases.ICreateCategoryUseCase;
import org.solen.business.categorycases.IDeleteCategoryUseCase;
import org.solen.business.categorycases.IGetCategoryByIdUseCase;
import org.solen.business.categorycases.IGetCategoryTreeUseCase;
import org.solen.business.categorycases.IUpdateCategoryUseCase;
import org.solen.controller.dto.category.CategoryResponse;
import org.solen.controller.dto.category.CreateCategoryRequest;
import org.solen.controller.dto.category.UpdateCategoryRequest;
import org.solen.controller.mappers.CategoryMapper;
import org.solen.domain.practices.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CategoryController {

    private ICreateCategoryUseCase createCategoryUseCase;
    private IUpdateCategoryUseCase updateCategoryUseCase;
    private IDeleteCategoryUseCase deleteCategoryUseCase;
    private IGetCategoryTreeUseCase getCategoryTreeUseCase;
    private IGetCategoryByIdUseCase getCategoryByIdUseCase;
    private CategoryMapper categoryMapper;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategoryTree() {
        List<Category> categories = getCategoryTreeUseCase.getCategoryTree();
        return ResponseEntity.ok(
                categories.stream().map(categoryMapper::convertToResponse).toList()
        );
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        Category category = getCategoryByIdUseCase.getCategoryById(id);
        return ResponseEntity.ok(categoryMapper.convertToResponse(category));
    }

    @PostMapping("/admin/categories")
    @PreAuthorize("@categorySecurity.isAdminByEmail(authentication.name)")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        Category category = createCategoryUseCase.createCategory(request);
        return ResponseEntity.ok(categoryMapper.convertToResponse(category));
    }

    @PutMapping("/admin/categories/{id}")
    @PreAuthorize("@categorySecurity.isAdminByEmail(authentication.name)")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        Category category = updateCategoryUseCase.updateCategory(id, request);
        return ResponseEntity.ok(categoryMapper.convertToResponse(category));
    }

    @DeleteMapping("/admin/categories/{id}")
    @PreAuthorize("@categorySecurity.isAdminByEmail(authentication.name)")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        deleteCategoryUseCase.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
