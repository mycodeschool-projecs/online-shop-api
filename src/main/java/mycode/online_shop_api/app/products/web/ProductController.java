package mycode.online_shop_api.app.products.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import mycode.online_shop_api.app.global_exceptions.ApiError;
import mycode.online_shop_api.app.products.dto.*;
import mycode.online_shop_api.app.products.service.ProductCommandService;
import mycode.online_shop_api.app.products.service.ProductQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for product catalogue management.
 * <p>
 * Security convention:
 *   – GrantedAuthority values: ROLE_ADMIN, ROLE_CLIENT
 *   – Therefore annotations use hasRole/hasAnyRole without the ROLE_ prefix.
 * <p>
 * Base path: /api/v1/products
 */
@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products")
public class ProductController {

    private final ProductQueryService productQueryService;
    private final ProductCommandService productCommandService;

    /* ------------------------------------------------------------------ */
    /* Commands                                                            */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    @Operation(summary = "Create product",
            description = "Adds a product to the catalogue and links it to its category. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Category missing",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productCommandService.addProduct(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("edit/{productId}")
    @Operation(summary = "Update product",
            description = "Replaces mutable product fields such as price, stock or description. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int productId,
                                                         @Valid @RequestBody UpdateProductRequest request) {
        productCommandService.updateProductPut(productId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(productQueryService.findById(productId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{productId}")
    @Operation(summary = "Delete product",
            description = "Removes a product and any of its category relations. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Product scheduled for deletion"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable int productId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(productCommandService.deleteProduct(productId));
    }

    /* ------------------------------------------------------------------ */
    /* Queries                                                             */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @GetMapping("/all")
    @Operation(summary = "List products",
            description = "Returns every product regardless of availability. Requires roles: ADMIN or CLIENT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ProductResponseList> getAllProducts() {
        return ResponseEntity.ok(productQueryService.getAllProducts());
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @GetMapping("/most-sold")
    @Operation(summary = "Top selling products",
            description = "Ranks products by quantity sold based on order history. Requires roles: ADMIN or CLIENT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ProductResponseList> getMostSoldProducts() {
        return ResponseEntity.ok(productQueryService.getTopSellingProducts());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total")
    @Operation(summary = "Total products", description = "Counts all products stored in the catalogue. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Integer> totalProducts() {
        return ResponseEntity.ok(productQueryService.totalProducts());
    }
}
