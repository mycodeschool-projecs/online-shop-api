package mycode.online_shop_api.app.orders.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    /* ------------------------------------------------------------------ */
    /* Commands                                                           */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @PostMapping("/sendOrder")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderCommandService.addOrder(createOrderRequest));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @PutMapping("/cancelOrder/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable int orderId) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(orderCommandService.cancelOrder(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteOrder/{orderId}")
    public ResponseEntity<OrderResponse> deleteOrder(@PathVariable int orderId) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(orderCommandService.deleteOrder(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateOrder/{orderId}")
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
    public ResponseEntity<OrderResponseList> getRecentOrders() {
        return ResponseEntity.ok(orderQueryService.getRecentOrders());
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @GetMapping("/getCustomerOrders")
    public ResponseEntity<OrderResponseList> getCustomerOrders() {
        return ResponseEntity.ok(orderQueryService.customerOrders());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/totalOrders")
    public ResponseEntity<Integer> totalOrders() {
        return ResponseEntity.ok(orderQueryService.totalOrders());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/totalRevenue")
    public ResponseEntity<Double> totalRevenue() {
        return ResponseEntity.ok(orderQueryService.totalRevenue());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Double>> getMonthlyRevenue() {
        return ResponseEntity.ok(orderQueryService.getMonthlyRevenue());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<OrderResponseList> getAllOrders() {
        return ResponseEntity.ok(orderQueryService.getAllOrders());
    }
}
