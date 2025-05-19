package mycode.online_shop_api.app.orders.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import mycode.online_shop_api.app.orders.dtos.CreateOrderRequest;
import mycode.online_shop_api.app.orders.dtos.CreateOrderUpdateRequest;
import mycode.online_shop_api.app.orders.dtos.OrderResponse;
import mycode.online_shop_api.app.orders.dtos.OrderResponseList;
import mycode.online_shop_api.app.orders.service.OrderCommandService;
import mycode.online_shop_api.app.orders.service.OrderQueryService;
import mycode.online_shop_api.app.system.jwt.JWTAuthorizationFilter;
import mycode.online_shop_api.app.system.jwt.JWTTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderCommandService orderCommandService;

    @MockBean
    private OrderQueryService orderQueryService;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @MockBean
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/order/sendOrder - should return 201 CREATED")
    void createOrder() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest(LocalDate.now(), List.of());
        OrderResponse response = new OrderResponse(1, "test@example.com", "Shipping Address", "Order Address", LocalDate.now(), 100.0, "Pending", null);

        when(orderCommandService.addOrder(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/order/sendOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/order/cancelOrder/{orderId} - should return 202 ACCEPTED")
    void cancelOrder() throws Exception {
        OrderResponse response = new OrderResponse(1, "test@example.com", "Shipping Address", "Order Address", LocalDate.now(), 100.0, "Cancelled", null);

        when(orderCommandService.cancelOrder(1)).thenReturn(response);

        mockMvc.perform(put("/api/v1/order/cancelOrder/1"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.orderStatus").value("Cancelled"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/order/deleteOrder/{orderId} - should return 202 ACCEPTED")
    void deleteOrder() throws Exception {
        OrderResponse response = new OrderResponse(1, "test@example.com", "Shipping Address", "Order Address", LocalDate.now(), 100.0, "Deleted", null);

        when(orderCommandService.deleteOrder(1)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/order/deleteOrder/1"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.orderStatus").value("Deleted"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/order/updateOrder/{orderId} - should update an order")
    void updateOrder() throws Exception {
        CreateOrderUpdateRequest request = new CreateOrderUpdateRequest("test@example.com", "Shipping Address", "Order Address", 100.0, "Shipped");
        OrderResponse response = new OrderResponse(1, "test@example.com", "Shipping Address", "Order Address", LocalDate.now(), 100.0, "Shipped", null);

        when(orderCommandService.updateOrder(anyInt(), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/order/updateOrder/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.orderStatus").value("Shipped"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/order/totalOrders - should return total count")
    void totalOrders() throws Exception {
        when(orderQueryService.totalOrders()).thenReturn(10);

        mockMvc.perform(get("/api/v1/order/totalOrders"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/order/all - should return all orders")
    void getAllOrders() throws Exception {
        when(orderQueryService.getAllOrders()).thenReturn(new OrderResponseList(List.of()));

        mockMvc.perform(get("/api/v1/order/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/order/totalRevenue - should return total revenue")
    void totalRevenue() throws Exception {
        when(orderQueryService.totalRevenue()).thenReturn(5000.0);

        mockMvc.perform(get("/api/v1/order/totalRevenue"))
                .andExpect(status().isOk())
                .andExpect(content().string("5000.0"));
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/order/monthly - should return monthly revenue")
    void getMonthlyRevenue() throws Exception {
        when(orderQueryService.getMonthlyRevenue()).thenReturn(Map.of("January", 1000.0));

        mockMvc.perform(get("/api/v1/order/monthly"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.January").value(1000.0));
    }
}
