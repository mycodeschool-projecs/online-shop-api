package mycode.online_shop_api.app.orders.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mycode.online_shop_api.app.global_exceptions.ApiError;
import mycode.online_shop_api.app.orders.dtos.CreateOrderRequest;
import mycode.online_shop_api.app.orders.dtos.CreateOrderUpdateRequest;
import mycode.online_shop_api.app.orders.dtos.OrderResponse;
import mycode.online_shop_api.app.orders.dtos.OrderResponseList;
import mycode.online_shop_api.app.orders.service.OrderCommandService;
import mycode.online_shop_api.app.orders.service.OrderQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller that exposes commands and queries for orders.
 * <p>
 * Role strategy:
 *   - Roles in the DB: ADMIN, CLIENT (no ROLE_ prefix)
 *   - GrantedAuthority values: ROLE_ADMIN, ROLE_CLIENT
 *   - Method security annotations therefore use hasRole / hasAnyRole without the prefix.
 */
@RestController
@RequestMapping("/api/v1/order")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Orders")
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    /* ------------------------------------------------------------------ */
    /* Commands                                                           */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @PostMapping("/sendOrder")
    @Operation(summary = "Place order",
            description = "Creates a new order from the provided cart items. Requires roles: ADMIN or CLIENT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Cart empty",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderCommandService.addOrder(createOrderRequest));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @PutMapping("/cancelOrder/{orderId}")
    @Operation(summary = "Cancel order",
            description = "Cancels an existing order and removes its items. Requires roles: ADMIN or CLIENT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable int orderId) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(orderCommandService.cancelOrder(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteOrder/{orderId}")
    @Operation(summary = "Delete order",
            description = "Permanently deletes an order. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderResponse> deleteOrder(@PathVariable int orderId) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(orderCommandService.deleteOrder(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateOrder/{orderId}")
    @Operation(summary = "Update order",
            description = "Replaces mutable order fields such as addresses or amount. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable int orderId,
            @RequestBody CreateOrderUpdateRequest createOrderUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(orderCommandService.updateOrder(orderId, createOrderUpdateRequest));
    }

    /* ------------------------------------------------------------------ */
    /* Queries                                                            */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getRecentOrders")
    @Operation(summary = "Recent orders",
            description = "Returns the 10 most recent orders for dashboard widgets. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderResponseList> getRecentOrders() {
        return ResponseEntity.ok(orderQueryService.getRecentOrders());
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @GetMapping("/getCustomerOrders")
    @Operation(summary = "My orders",
            description = "Returns the authenticated customer's order history. Requires roles: ADMIN or CLIENT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderResponseList> getCustomerOrders() {
        return ResponseEntity.ok(orderQueryService.customerOrders());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/totalOrders")
    @Operation(summary = "Total orders", description = "Count of all orders in the system. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Integer> totalOrders() {
        return ResponseEntity.ok(orderQueryService.totalOrders());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/totalRevenue")
    @Operation(summary = "Total revenue", description = "Aggregate revenue over all time. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Double> totalRevenue() {
        return ResponseEntity.ok(orderQueryService.totalRevenue());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/monthly")
    @Operation(summary = "Monthly revenue",
            description = "Revenue breakdown for the last 12 months. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Map<String, Double>> getMonthlyRevenue() {
        return ResponseEntity.ok(orderQueryService.getMonthlyRevenue());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    @Operation(summary = "All orders",
            description = "Returns every order for administration purposes. Requires role: ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderResponseList> getAllOrders() {
        return ResponseEntity.ok(orderQueryService.getAllOrders());
    }
}
