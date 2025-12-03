package mycode.online_shop_api.app.categories.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mycode.online_shop_api.app.categories.dtos.*;
import mycode.online_shop_api.app.categories.service.CategoryCommandService;
import mycode.online_shop_api.app.categories.service.CategoryQueryService;
import mycode.online_shop_api.app.global_exceptions.ApiError;
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
@Tag(name = "Categories")
public class CategoryController {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    /* ------------------------------------------------------------------ */
    /* Commands                                                            */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create category",
            description = "Registers a top-level category that products can be mapped to. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created"),
            @ApiResponse(responseCode = "409", description = "Category already exists",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<CategoryResponse> addCategory(
            @RequestBody CreateCategoryRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryCommandService.addCategory(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{parentId}/subcategories")
    @Operation(summary = "Create subcategory",
            description = "Appends a subcategory beneath an existing parent. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CategoryResponse> addSubcategory(
            @PathVariable int parentId,
            @RequestBody CreateCategoryRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryCommandService.addSubcategory(parentId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    @Operation(summary = "Rename category",
            description = "Updates the display name for a category. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable int categoryId,
            @RequestBody UpdateCategoryRequest request) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(categoryCommandService.updateCategory(categoryId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete category",
            description = "Deletes a category and detaches product links. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
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
    @Operation(summary = "List categories",
            description = "Returns every category including its parent reference. Requires roles: ADMIN or CLIENT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CategoryResponseList> getAll() {
        return ResponseEntity.ok(categoryQueryService.getAllCategories());
    }
}
