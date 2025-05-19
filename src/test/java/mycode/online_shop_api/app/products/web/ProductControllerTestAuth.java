package mycode.online_shop_api.app.products.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import mycode.online_shop_api.app.products.dto.*;
import mycode.online_shop_api.app.products.mocks.ProductMockData;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.service.ProductCommandService;
import mycode.online_shop_api.app.products.service.ProductQueryService;
import mycode.online_shop_api.app.system.jwt.JWTAuthorizationFilter;
import mycode.online_shop_api.app.system.jwt.JWTTokenProvider;
import mycode.online_shop_api.app.mock.SecurityMockFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTestAuth {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductCommandService productCommandService;

    @MockBean
    private ProductQueryService productQueryService;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @MockBean
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @BeforeEach
    void setUp() {


        SecurityMockFactory.setupTokens(jwtTokenProvider);


        SecurityMockFactory.mockAdminToken(jwtTokenProvider);
        SecurityMockFactory.mockClientToken(jwtTokenProvider);
        SecurityMockFactory.mockInvalidToken(jwtTokenProvider);
    }


    // todo: fix test
//    @Test
//    @DisplayName("POST /product/addProduct - ADMIN -> 201 Created")
//    void addProduct_admin() throws Exception {
//        CreateProductRequest request = new CreateProductRequest("Laptop", "Gaming", "Gaming Laptop", 2000, 3, 3.0);
//        Product product = ProductMockData.createGamingLaptop();
//        product.setId(1);
//        ProductResponse response = new ProductResponse(product.getId(), product.getCategory(), product.getCreateDate(),
//                product.getDescriptions(), product.getName(), product.getPrice(), product.getStock(), product.getWeight());
//
//        when(productCommandService.addProduct(any())).thenReturn(response);
//
//        mockMvc.perform(post("/product/addProduct")
//                        .header("Authorization", "Bearer " + SecurityMockFactory.ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value("Gaming Laptop"));
//    }

    @Test
    @DisplayName("POST /api/v1/products/add - CLIENT -> 403 Forbidden")
    void addProduct_client_forbidden() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Laptop", "Gaming", "Gaming Laptop", 2000, 3, 3.0);

        mockMvc.perform(post("/api/v1/products/add")
                        .header("Authorization", "Bearer " + SecurityMockFactory.CLIENT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/v1/products/add - Invalid Token -> 403 Forbidden")
    void addProduct_invalidToken_forbidden() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Laptop", "Gaming", "Gaming Laptop", 2000, 3, 3.0);

        mockMvc.perform(post("/api/v1/products/add")
                        .header("Authorization", "Bearer " + SecurityMockFactory.INVALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }


    // todo: fix test
//    @Test
//    @DisplayName("DELETE /product/{id} - ADMIN -> 202 Accepted")
//    void deleteProduct_admin() throws Exception {
//        Product product = ProductMockData.createCheapLaptop();
//        product.setId(1);
//        ProductResponse response = new ProductResponse(product.getId(), product.getCategory(), product.getCreateDate(),
//                product.getDescriptions(), product.getName(), product.getPrice(), product.getStock(), product.getWeight());
//
//        when(productCommandService.deleteProduct(1)).thenReturn(response);
//
//        mockMvc.perform(delete("/product/1")
//                        .header("Authorization", "Bearer " + SecurityMockFactory.ADMIN_TOKEN))
//                .andExpect(status().isAccepted())
//                .andExpect(jsonPath("$.name").value("Cheap Laptop"));
//    }


    // todo: fix test
//    @Test
//    @DisplayName("PUT /product/{id} - ADMIN -> 202 Accepted")
//    void updateProduct_admin() throws Exception {
//        UpdateProductRequest update = new UpdateProductRequest("UpdatedCat", "UpdatedDesc", "Updated", 888, 15, 1.5);
//        Product product = ProductMockData.createCheapLaptop();
//        product.setId(1);
//        ProductResponse response = new ProductResponse(product.getId(), product.getCategory(), product.getCreateDate(),
//                product.getDescriptions(), product.getName(), product.getPrice(), product.getStock(), product.getWeight());
//
//        doNothing().when(productCommandService).updateProductPut(eq(1), any());
//        when(productQueryService.findById(1)).thenReturn(response);
//
//        mockMvc.perform(put("/product/1")
//                        .header("Authorization", "Bearer " + SecurityMockFactory.ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(update)))
//                .andExpect(status().isAccepted())
//                .andExpect(jsonPath("$.name").value("Cheap Laptop"));
//    }


    // todo: fix test
//    @Test
//    @DisplayName("GET /product/totalProducts - ADMIN -> 200 OK")
//    void totalProducts_admin() throws Exception {
//        when(productQueryService.totalProducts()).thenReturn(7);
//
//        mockMvc.perform(get("/product/totalProducts")
//                        .header("Authorization", "Bearer " + SecurityMockFactory.ADMIN_TOKEN))
//                .andExpect(status().isOk())
//                .andExpect(content().string("7"));
//    }


    // todo: fix test
//    @Test
//    @DisplayName("GET /product/getAllProducts - CLIENT -> 200 OK")
//    void getAllProducts_client() throws Exception {
//        List<Product> products = ProductMockData.createSampleProducts();
//        ProductResponseList responseList = new ProductResponseList(products.stream()
//                .map(p -> new ProductResponse(p.getId(), p.getCategory(), p.getCreateDate(), p.getDescriptions(),
//                        p.getName(), p.getPrice(), p.getStock(), p.getWeight()))
//                .toList());
//
//        when(productQueryService.getAllProducts()).thenReturn(responseList);
//
//        mockMvc.perform(get("/product/getAllProducts")
//                        .header("Authorization", "Bearer " + SecurityMockFactory.CLIENT_TOKEN))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.list.length()").value(3));
//    }

    // todo: fix test
//    @Test
//    @DisplayName("GET /product/mostSold - ADMIN -> 200 OK")
//    void getMostSold_admin() throws Exception {
//        List<Product> products = ProductMockData.createSampleProducts();
//        ProductResponseList responseList = new ProductResponseList(products.stream()
//                .map(p -> new ProductResponse(p.getId(), p.getCategory(), p.getCreateDate(), p.getDescriptions(),
//                        p.getName(), p.getPrice(), p.getStock(), p.getWeight()))
//                .toList());
//
//        when(productQueryService.getTopSellingProducts()).thenReturn(responseList);
//
//        mockMvc.perform(get("/product/mostSold")
//                        .header("authorization", "Bearer " + SecurityMockFactory.ADMIN_TOKEN))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.list.length()").value(3));
//    }
}