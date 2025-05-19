package mycode.online_shop_api.app.products.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import mycode.online_shop_api.app.products.dto.CreateProductRequest;
import mycode.online_shop_api.app.products.dto.ProductResponse;
import mycode.online_shop_api.app.products.dto.ProductResponseList;
import mycode.online_shop_api.app.products.dto.UpdateProductRequest;
import mycode.online_shop_api.app.products.mocks.ProductMockData;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.service.ProductCommandService;
import mycode.online_shop_api.app.products.service.ProductQueryService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductCommandService productCommandService;

    @MockBean
    private ProductQueryService productQueryService;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @MockBean
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("POST /api/v1/products/add - should return 201 CREATED")
    void addProduct() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Laptop", "Gaming Beast", "Gaming Laptop", 2000, 3, 3.0);
        Product product = ProductMockData.createGamingLaptop();
        product.setId(1);
        ProductResponse response = new ProductResponse(product.getId(), product.getCategory(), product.getCreateDate(),
                product.getDescriptions(), product.getName(), product.getPrice(), product.getStock(), product.getWeight());

        when(productCommandService.addProduct(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Gaming Laptop"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/products/delete/{id} - should return 202 ACCEPTED")
    void deleteProduct() throws Exception {
        Product product = ProductMockData.createCheapLaptop();
        product.setId(1);
        ProductResponse response = new ProductResponse(product.getId(), product.getCategory(), product.getCreateDate(),
                product.getDescriptions(), product.getName(), product.getPrice(), product.getStock(), product.getWeight());

        when(productCommandService.deleteProduct(1)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/products/delete/1"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("Cheap Laptop"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/products/edit/{id} - should update and return the updated product")
    void updateProductPut() throws Exception {
        UpdateProductRequest updateRequest = new UpdateProductRequest("UpdatedCat", "UpdatedDesc", "Updated", 888, 15, 1.5);
        Product product = ProductMockData.createCheapLaptop();
        product.setId(1);
        ProductResponse response = new ProductResponse(product.getId(), product.getCategory(), product.getCreateDate(),
                product.getDescriptions(), product.getName(), product.getPrice(), product.getStock(), product.getWeight());

        doNothing().when(productCommandService).updateProductPut(eq(1), any());
        when(productQueryService.findById(1)).thenReturn(response);

        mockMvc.perform(put("/api/v1/products/edit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("Cheap Laptop"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/products/total - should return total count")
    void totalProducts() throws Exception {
        when(productQueryService.totalProducts()).thenReturn(7);

        mockMvc.perform(get("/api/v1/products/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("7"));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("GET /api/v1/products/all - should return list of products")
    void getAllProducts() throws Exception {
        List<Product> products = ProductMockData.createSampleProducts();
        ProductResponseList responseList = new ProductResponseList(products.stream()
                .map(p -> new ProductResponse(p.getId(), p.getCategory(), p.getCreateDate(), p.getDescriptions(),
                        p.getName(), p.getPrice(), p.getStock(), p.getWeight()))
                .toList());

        when(productQueryService.getAllProducts()).thenReturn(responseList);

        mockMvc.perform(get("/api/v1/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(3));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/products/most-sold - should return top selling products")
    void getMostSoldProducts() throws Exception {
        List<Product> products = ProductMockData.createSampleProducts();
        ProductResponseList responseList = new ProductResponseList(products.stream()
                .map(p -> new ProductResponse(p.getId(), p.getCategory(), p.getCreateDate(), p.getDescriptions(),
                        p.getName(), p.getPrice(), p.getStock(), p.getWeight()))
                .toList());

        when(productQueryService.getTopSellingProducts()).thenReturn(responseList);

        mockMvc.perform(get("/api/v1/products/most-sold"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(3));
    }
}