package mycode.online_shop_api.app.cart.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mycode.online_shop_api.app.cart.dtos.AddProductToCartRequest;
import mycode.online_shop_api.app.cart.dtos.CartResponse;
import mycode.online_shop_api.app.cart.dtos.UpdateCartQuantityRequest;
import mycode.online_shop_api.app.cart.services.CartCommandService;
import mycode.online_shop_api.app.cart.services.CartQueryService;
import mycode.online_shop_api.app.global_exceptions.ApiError;
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
@Tag(name = "Cart")
public class CartController {

    private final CartCommandService cartCommandService;
    private final CartQueryService cartQueryService;

    /* ------------------------------------------------------------------ */
    /* Queries                                                             */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @GetMapping("/all")
    @Operation(summary = "Current user's cart",
            description = "Returns the authenticated user's cart with product breakdown. Requires roles: ADMIN or CLIENT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartQueryService.getCart());
    }

    /* ------------------------------------------------------------------ */
    /* Commands                                                            */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @PostMapping("/products")
    @Operation(summary = "Add product to cart",
            description = "Adds or increments a product in the authenticated user's cart. Requires roles: ADMIN or CLIENT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product added"),
            @ApiResponse(responseCode = "404", description = "Product missing",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<CartResponse> addProductToCart(@RequestBody AddProductToCartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartCommandService.addProductToCart(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @PutMapping("/products/{productId}")
    @Operation(summary = "Update product quantity",
            description = "Overrides the quantity for a product inside the cart. Requires roles: ADMIN or CLIENT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CartResponse> updateProductQuantity(@PathVariable int productId,
                                                              @RequestBody UpdateCartQuantityRequest request) {
        return ResponseEntity.ok(cartCommandService.updateCartQuantity(request, productId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @DeleteMapping("/products/{productId}")
    @Operation(summary = "Remove product from cart",
            description = "Removes a product entirely from the cart. Requires roles: ADMIN or CLIENT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CartResponse> deleteProductFromCart(@PathVariable int productId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(cartCommandService.deleteProductFromCart(productId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @DeleteMapping("/delete")
    @Operation(summary = "Empty cart",
            description = "Deletes every cart item for the authenticated user. Requires roles: ADMIN or CLIENT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> emptyUserCart() {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(cartCommandService.emptyUserCart());
    }
}
