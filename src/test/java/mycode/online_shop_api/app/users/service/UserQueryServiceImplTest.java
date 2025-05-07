package mycode.online_shop_api.app.users.service;

import mycode.online_shop_api.app.orders.repository.OrderRepository;
import mycode.online_shop_api.app.users.dtos.UserResponse;
import mycode.online_shop_api.app.users.dtos.UserResponseList;
import mycode.online_shop_api.app.users.exceptions.NoUserFound;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserQueryServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UserQueryServiceImpl userQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindUserById() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .fullName("Test User")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userQueryService.findUserById(1L);

        assertEquals("test@example.com", response.email());
        assertEquals("Test User", response.fullName());
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoUserFound.class, () -> userQueryService.findUserById(99L));
        verify(userRepository).findById(99L);
    }

    @Test
    void shouldReturnAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(User.builder().id(1L).email("user1@example.com").fullName("User One").build());
        users.add(User.builder().id(2L).email("user2@example.com").fullName("User Two").build());

        when(userRepository.findAll()).thenReturn(users);

        UserResponseList responseList = userQueryService.getAllUsers();

        assertEquals(2, responseList.list().size());
        assertEquals("user1@example.com", responseList.list().get(0).email());
        assertEquals("user2@example.com", responseList.list().get(1).email());
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        UserResponseList responseList = userQueryService.getAllUsers();

        assertTrue(responseList.list().isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void shouldFindUserByEmail() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .fullName("Test User")
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User foundUser = userQueryService.findByEmail("test@example.com");

        assertEquals("test@example.com", foundUser.getEmail());
        assertEquals("Test User", foundUser.getFullName());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(NoUserFound.class, () -> userQueryService.findByEmail("nonexistent@example.com"));
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void shouldReturnMostActiveUsers() {
        List<User> activeUsers = new ArrayList<>();
        activeUsers.add(User.builder().id(1L).email("active1@example.com").fullName("Active One").build());
        activeUsers.add(User.builder().id(2L).email("active2@example.com").fullName("Active Two").build());

        when(orderRepository.findMostActiveUsers()).thenReturn(Optional.of(activeUsers));

        UserResponseList responseList = userQueryService.getMostActiveUsers();

        assertEquals(2, responseList.list().size());
        assertEquals("active1@example.com", responseList.list().get(0).email());
        assertEquals("active2@example.com", responseList.list().get(1).email());
        verify(orderRepository).findMostActiveUsers();
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveUsersFound() {
        when(orderRepository.findMostActiveUsers()).thenReturn(Optional.empty());

        UserResponseList responseList = userQueryService.getMostActiveUsers();

        assertTrue(responseList.list().isEmpty());
        verify(orderRepository).findMostActiveUsers();
    }

    @Test
    void shouldReturnTotalUserCount() {
        List<User> users = new ArrayList<>();
        users.add(User.builder().build());
        users.add(User.builder().build());
        users.add(User.builder().build());

        when(userRepository.findAll()).thenReturn(users);

        int count = userQueryService.totalUsers();

        assertEquals(3, count);
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnZeroWhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        int count = userQueryService.totalUsers();

        assertEquals(0, count);
        verify(userRepository).findAll();
    }
}