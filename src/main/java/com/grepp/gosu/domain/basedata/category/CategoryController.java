package com.grepp.gosu.domain.basedata.category;

import com.grepp.gosu.domain.basedata.category.dto.CategoryRequest;
import com.grepp.gosu.domain.basedata.category.dto.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "카테고리", description = "카테고리 조회 및 관리자 카테고리 등록·수정·삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 목록 조회")
    @SecurityRequirements
    @GetMapping("/public/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories(@RequestParam(required = false) Long parentId) {
        List<CategoryResponse> response = categoryService.getCategories(parentId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카테고리 등록")
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "카테고리 수정")
    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long id) {
        CategoryResponse response = categoryService.deleteCategory(id);
        return ResponseEntity.ok(response);
    }
}
