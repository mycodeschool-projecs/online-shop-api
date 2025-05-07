package mycode.online_shop_api.app.cart.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mycode.online_shop_api.app.cart.dtos.AddProductToCartRequest;
import mycode.online_shop_api.app.cart.dtos.CartResponse;
import mycode.online_shop_api.app.cart.dtos.UpdateCartQuantityRequest;
import mycode.online_shop_api.app.cart.services.CartCommandService;
import mycode.online_shop_api.app.cart.services.CartQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for cart operations.
 * <p>
 * Security convention:
 *   - GrantedAuthority values: ROLE_ADMIN, ROLE_CLIENT
 *   - Therefore use hasAnyRole('ADMIN','CLIENT').
 */
@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartCommandService cartCommandService;
    private final CartQueryService cartQueryService;

    /* ------------------------------------------------------------------ */
    /* Queries                                                             */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @GetMapping("/all")
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartQueryService.getCart());
    }

    /* ------------------------------------------------------------------ */
    /* Commands                                                            */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @PostMapping("/products")
    public ResponseEntity<CartResponse> addProductToCart(@RequestBody AddProductToCartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartCommandService.addProductToCart(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @PutMapping("/products/{productId}")
    public ResponseEntity<CartResponse> updateProductQuantity(@PathVariable int productId,
                                                              @RequestBody UpdateCartQuantityRequest request) {
        return ResponseEntity.ok(cartCommandService.updateCartQuantity(request, productId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<CartResponse> deleteProductFromCart(@PathVariable int productId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(cartCommandService.deleteProductFromCart(productId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> emptyUserCart() {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(cartCommandService.emptyUserCart());
    }
}
