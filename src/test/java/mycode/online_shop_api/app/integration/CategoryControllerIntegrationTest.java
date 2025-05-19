package mycode.online_shop_api.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import mycode.online_shop_api.app.categories.dtos.CreateCategoryRequest;
import mycode.online_shop_api.app.categories.repository.CategoryRepository;
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

//@SpringBootTest
//@AutoConfigureMockMvc(addFilters = false)
//@ActiveProfiles("test")
//class CategoryControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @BeforeEach
//    void setup() {
//        categoryRepository.deleteAll();
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    @DisplayName("Should create and fetch categories")
//    void addAndFetchCategories() throws Exception {
//        CreateCategoryRequest request = new CreateCategoryRequest("Books");
//        mockMvc.perform(post("/api/v1/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated());
//
//        mockMvc.perform(get("/api/v1/categories/all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.list.length()").value(1));
//    }
//}
