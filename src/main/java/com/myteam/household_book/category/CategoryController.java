// 카테고리 조회 요청을 처리 컨트롤러

package com.myteam.household_book.category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getCategories(
            @RequestParam Long userId,
            @RequestParam String type) {
        try {
            CategoryResponse response = categoryService.getUserCategories(userId, type);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            CategoryResponse errorResponse = new CategoryResponse(false, 400, e.getMessage(), null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}