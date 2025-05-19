package mycode.online_shop_api.app.cart.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import mycode.online_shop_api.app.cart.dtos.AddProductToCartRequest;
import mycode.online_shop_api.app.cart.dtos.CartResponse;
import mycode.online_shop_api.app.cart.dtos.UpdateCartQuantityRequest;
import mycode.online_shop_api.app.cart.services.CartCommandService;
import mycode.online_shop_api.app.cart.services.CartQueryService;
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

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartCommandService cartCommandService;

    @MockBean
    private CartQueryService cartQueryService;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @MockBean
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("GET /api/v1/cart/all - should return cart data")
    void getCart() throws Exception {

        CartResponse mockCartResponse = CartResponse.builder().list(new ArrayList<>()).build();


        when(cartQueryService.getCart()).thenReturn(mockCartResponse);

        mockMvc.perform(get("/api/v1/cart/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("POST /api/v1/cart/products - should add product to cart")
    void addProductToCart() throws Exception {
        AddProductToCartRequest request = AddProductToCartRequest.builder().productId(1).quantity(2).build();


        CartResponse mockCartResponse = CartResponse.builder().list(new ArrayList<>()).build();

        when(cartCommandService.addProductToCart(any())).thenReturn(mockCartResponse);

        mockMvc.perform(post("/api/v1/cart/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("DELETE /api/v1/cart/products/{productId} - should remove product from cart")
    void deleteProductFromCart() throws Exception {

        CartResponse mockCartResponse = CartResponse.builder().list(new ArrayList<>()).build();

        when(cartCommandService.deleteProductFromCart(1)).thenReturn(mockCartResponse);

        mockMvc.perform(delete("/api/v1/cart/products/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("PUT /api/v1/cart/products/{productId} - should update product quantity")
    void updateProductQuantity() throws Exception {

        UpdateCartQuantityRequest request = new UpdateCartQuantityRequest(5);


        CartResponse mockCartResponse = CartResponse.builder().list(new ArrayList<>()).build();


        mockMvc.perform(put("/api/v1/cart/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("DELETE /api/v1/cart/delete - should empty the cart")
    void emptyUserCart() throws Exception {

        when(cartCommandService.emptyUserCart()).thenReturn("Cart emptied successfully");

        mockMvc.perform(delete("/api/v1/cart/delete"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Cart emptied successfully"));
    }


}