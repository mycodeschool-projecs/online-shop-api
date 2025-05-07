package mycode.online_shop_api.app.categories.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mycode.online_shop_api.app.categories.dtos.*;
import mycode.online_shop_api.app.categories.service.CategoryCommandService;
import mycode.online_shop_api.app.categories.service.CategoryQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for category management.
 *
 * Security convention:
 *   – GrantedAuthority values: ROLE_ADMIN, ROLE_CLIENT
 *   – Therefore we use hasRole / hasAnyRole without the ROLE_ prefix.
 *
 * API version prefix: /api/v1
 */
@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    /* ------------------------------------------------------------------ */
    /* Commands                                                            */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(
            @RequestBody CreateCategoryRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryCommandService.addCategory(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{parentId}/subcategories")
    public ResponseEntity<CategoryResponse> addSubcategory(
            @PathVariable int parentId,
            @RequestBody CreateCategoryRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryCommandService.addSubcategory(parentId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable int categoryId,
            @RequestBody UpdateCategoryRequest request) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(categoryCommandService.updateCategory(categoryId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable int categoryId) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(categoryCommandService.deleteCategory(categoryId));
    }

    /* ------------------------------------------------------------------ */
    /* Queries                                                             */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @GetMapping("all")
    public ResponseEntity<CategoryResponseList> getAll() {
        return ResponseEntity.ok(categoryQueryService.getAllCategories());
    }
}
