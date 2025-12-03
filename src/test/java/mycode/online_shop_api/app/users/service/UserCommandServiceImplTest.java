package mycode.online_shop_api.app.users.service;

import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.cart.repository.CartRepository;
import mycode.online_shop_api.app.system.security.UserRole;
import mycode.online_shop_api.app.users.dtos.CreateUserRequest;
import mycode.online_shop_api.app.users.dtos.UpdateUserRequest;
import mycode.online_shop_api.app.users.dtos.UserResponse;
import mycode.online_shop_api.app.users.exceptions.NoUserFound;
import mycode.online_shop_api.app.users.exceptions.UserAlreadyExists;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        String encodedPassword = "encodedPassword";
        CreateUserRequest request = new CreateUserRequest(
                "John Doe",
                "john@example.com",
                "password123",
                "+1234567890",
                "US",
                "123 Billing St",
                "123 Main St",
                "ADMIN"
        );

        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);

        User savedUser = User.builder()
                .id(1L)
                .email("john@example.com")
                .password(encodedPassword)
                .fullName("John Doe")
                .shippingAddress("123 Main St")
                .billingAddress("123 Billing St")
                .country("US")
                .phone("+1234567890")
                .userRole(UserRole.CLIENT)
                .build();

        when(userRepository.saveAndFlush(any(User.class))).thenReturn(savedUser);

        UserResponse response = userCommandService.createUser(request);

        assertEquals("john@example.com", response.email());
        assertEquals("John Doe", response.fullName());

        verify(cartRepository).saveAndFlush(any(Cart.class));
        verify(userRepository).saveAndFlush(any(User.class));
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest(
                "John Doe",
                "existing@example.com",
                "password123",
                "+1234567890",
                "US",
                "123 Billing St",
                "123 Main St",
                "ADMIN"
        );

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExists.class, () -> userCommandService.createUser(request));

        verify(userRepository, never()).saveAndFlush(any(User.class));
        verify(cartRepository, never()).saveAndFlush(any(Cart.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        User user = User.builder()
                .id(1L)
                .email("john@example.com")
                .fullName("John Doe")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userCommandService.deleteUser(1L);

        assertEquals("john@example.com", response.email());
        assertEquals("John Doe", response.fullName());

        verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoUserFound.class, () -> userCommandService.deleteUser(99L));

        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        User existingUser = User.builder()
                .id(1L)
                .email("old@example.com")
                .fullName("Old Name")
                .phone("oldphone")
                .billingAddress("old billing")
                .shippingAddress("old shipping")
                .country("oldcountry")
                .build();

        UpdateUserRequest updateRequest = new UpdateUserRequest(
                "New Name",
                "new@example.com",
                "newphone",
                "New Country",
                "123 New Billing St",
                "123 New Shipping St"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot("new@example.com", 1L)).thenReturn(false);

        UserResponse response = userCommandService.updateUser(updateRequest, 1L);

        assertEquals("new@example.com", response.email());
        assertEquals("New Name", response.fullName());
        assertEquals("newphone", response.phone());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User updatedUser = userCaptor.getValue();
        assertEquals("new@example.com", updatedUser.getEmail());
        assertEquals("New Name", updatedUser.getFullName());
        assertEquals("newphone", updatedUser.getPhone());
        assertEquals("123 New Billing St", updatedUser.getBillingAddress());
        assertEquals("123 New Shipping St", updatedUser.getShippingAddress());
        assertEquals("New Country", updatedUser.getCountry());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {
        User user1 = User.builder()
                .id(1L)
                .email("user1@example.com")
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@example.com")
                .build();

        UpdateUserRequest updateRequest = new UpdateUserRequest(
                "Updated Name",
                "user2@example.com",
                "phone",
                "country",
                "billing",
                "address"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.existsByEmailAndIdNot("user2@example.com", 1L)).thenReturn(true);

        assertThrows(UserAlreadyExists.class, () -> userCommandService.updateUser(updateRequest, 1L));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        UpdateUserRequest updateRequest = new UpdateUserRequest(
                "name",
                "email@example.com",
                "phone",
                "country",
                "billing",
                "shipping"
        );

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoUserFound.class, () -> userCommandService.updateUser(updateRequest, 99L));

        verify(userRepository, never()).save(any(User.class));
    }
}
