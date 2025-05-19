package mycode.online_shop_api.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import mycode.online_shop_api.app.categories.dtos.CreateCategoryRequest;
import mycode.online_shop_api.app.categories.repository.CategoryRepository;
import mycode.online_shop_api.app.products.dto.CreateProductRequest;
import mycode.online_shop_api.app.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProductCategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should create category and product then fetch all products")
    void addProductAndFetchAll() throws Exception {
        CreateCategoryRequest catReq = new CreateCategoryRequest("Electronics");
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(catReq)))
                .andExpect(status().isCreated());

        CreateProductRequest prodReq = new CreateProductRequest(
                "Electronics",
                "Sample product",
                "Phone",
                100.0,
                5,
                0.5);

        mockMvc.perform(post("/api/v1/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prodReq)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(1));
    }
}
