package mycode.online_shop_api.app.products.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mycode.online_shop_api.app.products.dto.*;
import mycode.online_shop_api.app.products.service.ProductCommandService;
import mycode.online_shop_api.app.products.service.ProductQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productCommandService.addProduct(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("edit/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int productId,
                                                         @Valid @RequestBody UpdateProductRequest request) {
        productCommandService.updateProductPut(productId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(productQueryService.findById(productId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable int productId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(productCommandService.deleteProduct(productId));
    }

    /* ------------------------------------------------------------------ */
    /* Queries                                                             */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @GetMapping("/all")
    public ResponseEntity<ProductResponseList> getAllProducts() {
        return ResponseEntity.ok(productQueryService.getAllProducts());
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @GetMapping("/most-sold")
    public ResponseEntity<ProductResponseList> getMostSoldProducts() {
        return ResponseEntity.ok(productQueryService.getTopSellingProducts());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total")
    public ResponseEntity<Integer> totalProducts() {
        return ResponseEntity.ok(productQueryService.totalProducts());
    }
}
