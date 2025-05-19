package mycode.online_shop_api.app.users.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import mycode.online_shop_api.app.system.jwt.JWTAuthorizationFilter;
import mycode.online_shop_api.app.system.jwt.JWTTokenProvider;
import mycode.online_shop_api.app.system.security.UserRole;
import mycode.online_shop_api.app.users.dtos.*;
import mycode.online_shop_api.app.users.mapper.UserMapper;
import mycode.online_shop_api.app.users.mock.UserMockData;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.service.UserCommandService;
import mycode.online_shop_api.app.users.service.UserQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static mycode.online_shop_api.app.system.constants.Constants.JWT_TOKEN_HEADER;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserCommandService userCommandService;

    @MockBean
    private UserQueryService userQueryService;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @MockBean
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void totalUsers() throws Exception{

        when(userQueryService.totalUsers()).thenReturn(5);

        mockMvc.perform(get("/api/v1/users/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void mostActiveUsers() throws Exception{

        List<User> userList = new ArrayList<>();

        userList.add(UserMockData.createUser("test", "test"));

        List<UserResponse> responses= new ArrayList<>();

        userList.forEach(user -> {
            responses.add(UserMapper.userToResponseDto(user));
        });

        when(userQueryService.getMostActiveUsers()).thenReturn(new UserResponseList(responses));

        mockMvc.perform(get("/api/v1/users/most-active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(1));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById() throws Exception{

        User user = UserMockData.createUser("user", "test");

        UserResponse userResponse= UserMapper.userToResponseDto(user);

        when(userQueryService.findUserById(1)).thenReturn(userResponse);

        mockMvc.perform(get("/api/v1/users/find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addUser() throws Exception{
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email("email")
                .fullName("name")
                .password("testPassword123")
                .country("")
                .shippingAddress("")
                .billingAddress("")
                .phone("")
                .build();

        User user = UserMockData.createUser("email", "name");
        user.setId(1);
        UserResponse userResponse= UserMapper.userToResponseDto(user);

        when(userCommandService.createUser(createUserRequest)).thenReturn(userResponse);
        mockMvc.perform(post("/api/v1/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("email"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser() throws Exception {

        User user = UserMockData.createUser("email", "name");

        user.setId(1);

        UserResponse userResponse = UserMapper.userToResponseDto(user);

        when(userCommandService.deleteUser(1)).thenReturn(userResponse);

        mockMvc.perform(delete("/api/v1/users/delete/1"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.email").value("email"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser() throws Exception {

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .email("updated@email.com")
                .fullName("Updated Name")
                .country("CountryName")
                .shippingAddress("Shipping Address")
                .billingAddress("Billing Address")
                .phone("123456789")
                .build();


        User updatedUser = UserMockData.createUser("updated@email.com", "Updated Name");
        updatedUser.setId(1);
        UserResponse userResponse = UserMapper.userToResponseDto(updatedUser);

        when(userCommandService.updateUser(any(UpdateUserRequest.class), eq(1L))).thenReturn(userResponse);

        mockMvc.perform(put("/api/v1/users/edit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.email").value("updated@email.com"))
                .andExpect(jsonPath("$.fullName").value("Updated Name"));

        verify(userCommandService).updateUser(any(UpdateUserRequest.class), eq(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers() throws Exception {

        List<User> userList = new ArrayList<>();
        userList.add(UserMockData.createUser("user1@test.com", "User One"));
        userList.add(UserMockData.createUser("user2@test.com", "User Two"));


        List<UserResponse> responses = new ArrayList<>();
        userList.forEach(user -> responses.add(UserMapper.userToResponseDto(user)));
        UserResponseList userResponseList = new UserResponseList(responses);

        when(userQueryService.getAllUsers()).thenReturn(userResponseList);

        mockMvc.perform(get("/api/v1/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(2))
                .andExpect(jsonPath("$.list[0].email").value("user1@test.com"))
                .andExpect(jsonPath("$.list[1].email").value("user2@test.com"));

        verify(userQueryService).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserRole_validToken() throws Exception {

        String token = "Bearer valid_token";
        String extractedToken = "valid_token";
        String username = "user@test.com";

        User user = UserMockData.createUser(username, "Test User");
        user.setUserRole(UserRole.ADMIN);


        when(jwtTokenProvider.getSubject(extractedToken)).thenReturn(username);
        when(jwtTokenProvider.isTokenValid(username, extractedToken)).thenReturn(true);
        when(userQueryService.findByEmail(username)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/role")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("ADMIN"));

        verify(jwtTokenProvider).getSubject(extractedToken);
        verify(jwtTokenProvider).isTokenValid(username, extractedToken);
        verify(userQueryService).findByEmail(username);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserRole_invalidToken() throws Exception {

        String token = "Bearer invalid_token";
        String extractedToken = "invalid_token";
        String username = "user@test.com";

        when(jwtTokenProvider.getSubject(extractedToken)).thenReturn(username);
        when(jwtTokenProvider.isTokenValid(username, extractedToken)).thenReturn(false);

        mockMvc.perform(get("/api/v1/users/role")
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid or expired token"));

        verify(jwtTokenProvider).getSubject(extractedToken);
        verify(jwtTokenProvider).isTokenValid(username, extractedToken);
        verify(userQueryService, never()).findByEmail(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserRole_invalidAuthorizationHeader() throws Exception {

        String token = "InvalidFormat token";

        mockMvc.perform(get("/api/v1/users/role")
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_successful() throws Exception {
        LoginRequest loginRequest = new LoginRequest("user@test.com", "password123");

        User user = UserMockData.createUser("user@test.com", "Test User");
        user.setId(1);
        user.setPhone("123456789");
        user.setUserRole(UserRole.CLIENT);

        String jwtToken = "jwt_token_value";
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtToken);


        LoginResponse expectedResponse = new LoginResponse(
                jwtToken,
                user.getId(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getUserRole()
        );

        when(userQueryService.findByEmail("user@test.com")).thenReturn(user);
        when(jwtTokenProvider.generateJWTToken(any(User.class))).thenReturn(jwtToken);

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").value(jwtToken))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Test User"))
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andExpect(jsonPath("$.userRole").value("CLIENT"));

        verify(authenticationManager).authenticate(any());
        verify(jwtTokenProvider).generateJWTToken(any(User.class));
    }

    @Test
    void register_successful() throws Exception {

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email("newuser@test.com")
                .fullName("New User")
                .password("password123")
                .country("Country")
                .shippingAddress("Shipping Address")
                .billingAddress("Billing Address")
                .phone("987654321")
                .build();

        User user = UserMockData.createUser("newuser@test.com", "New User");
        user.setPhone("987654321");
        user.setUserRole(UserRole.CLIENT);

        String jwtToken = "jwt_token_value";
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtToken);

        RegisterResponse expectedResponse = new RegisterResponse(
                jwtToken,
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getUserRole()
        );

        when(userQueryService.findByEmail("newuser@test.com")).thenReturn(user);
        when(jwtTokenProvider.generateJWTToken(any(User.class))).thenReturn(jwtToken);

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwtToken").value(jwtToken))
                .andExpect(jsonPath("$.fullName").value("New User"))
                .andExpect(jsonPath("$.email").value("newuser@test.com"))
                .andExpect(jsonPath("$.userRole").value("CLIENT"));

        verify(userCommandService).createUser(any(CreateUserRequest.class));
        verify(jwtTokenProvider).generateJWTToken(any(User.class));
    }
}
