package mycode.online_shop_api.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import mycode.online_shop_api.app.users.dtos.CreateUserRequest;
import mycode.online_shop_api.app.users.repository.UserRepository;
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
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should register user and fetch all users")
    @WithMockUser(roles = "ADMIN")
    void registerAndFetchUsers() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .fullName("Test User")
                .email("user@example.com")
                .password("pass")
                .phone("123456")
                .country("Country")
                .billingAddress("Addr1")
                .shippingAddress("Addr2")
                .userRole("ADMIN")
                .build();

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(1));
    }

    @Test
    @DisplayName("Should return conflict when registering duplicate email")
    void registerDuplicateEmailReturnsConflict() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .fullName("Dup User")
                .email("dup@example.com")
                .password("password")
                .phone("111222333")
                .country("Country")
                .billingAddress("Billing")
                .shippingAddress("Shipping")
                .userRole("CLIENT")
                .build();

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("User with this email already exists"));
    }
}
