package com.grepp.gosu.domain.basedata.category;

import com.grepp.gosu.domain.basedata.category.docs.CategoryControllerDocs;

import com.grepp.gosu.domain.basedata.category.dto.CategoryRequest;
import com.grepp.gosu.domain.basedata.category.dto.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController implements CategoryControllerDocs {

    private final CategoryService categoryService;

    @GetMapping("/public/categories")
    @Override
    public ResponseEntity<List<CategoryResponse>> getCategories(@RequestParam(required = false) Long parentId) {
        List<CategoryResponse> response = categoryService.getCategories(parentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/categories")
    @Override
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/admin/categories/{id}")
    @Override
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/categories/{id}")
    @Override
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long id) {
        CategoryResponse response = categoryService.deleteCategory(id);
        return ResponseEntity.ok(response);
    }
}
