package mycode.online_shop_api.app.categories.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import mycode.online_shop_api.app.categories.dtos.*;
import mycode.online_shop_api.app.categories.service.CategoryCommandService;
import mycode.online_shop_api.app.categories.service.CategoryQueryService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryCommandService categoryCommandService;

    @MockBean
    private CategoryQueryService categoryQueryService;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @MockBean
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/categories - should return 201 CREATED")
    void addCategory() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("Electronics");
        CategoryResponse response = new CategoryResponse(1, "Electronics", null);

        when(categoryCommandService.addCategory(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/v1/categories/{id} - should return 202 ACCEPTED")
    void deleteCategory() throws Exception {
        CategoryResponse response = new CategoryResponse(1, "Electronics", null);

        when(categoryCommandService.deleteCategory(1)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/categories/1"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /api/v1/categories/{id} - should return 202 ACCEPTED")
    void updateCategory() throws Exception {
        UpdateCategoryRequest request = new UpdateCategoryRequest("Updated Electronics");
        CategoryResponse response = new CategoryResponse(1, "Updated Electronics", null);

        when(categoryCommandService.updateCategory(1, request)).thenReturn(response);

        mockMvc.perform(put("/api/v1/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("Updated Electronics"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/categories/all - should return 200 OK")
    void getAllCategories() throws Exception {
        CategoryResponseList responseList = new CategoryResponseList(new ArrayList<>());

        when(categoryQueryService.getAllCategories()).thenReturn(responseList);

        mockMvc.perform(get("/api/v1/categories/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/categories/{parentId}/subcategories - should return 201 CREATED")
    void addSubcategory() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("Laptops");
        CategoryResponse response = new CategoryResponse(2, "Laptops", CategoryParentResponse.builder().id(1).name("test").build());

        when(categoryCommandService.addSubcategory(1, request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/categories/1/subcategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptops"));
    }
}
