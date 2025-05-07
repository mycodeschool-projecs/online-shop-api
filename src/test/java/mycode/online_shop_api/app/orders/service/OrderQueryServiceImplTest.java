package mycode.online_shop_api.app.orders.service;

import mycode.online_shop_api.app.orders.dtos.OrderResponse;
import mycode.online_shop_api.app.orders.dtos.OrderResponseList;
import mycode.online_shop_api.app.orders.mock.OrderMockData;
import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.orders.repository.OrderRepository;
import mycode.online_shop_api.app.users.exceptions.NoUserFound;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderQueryServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderQueryServiceImpl orderQueryService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("test@example.com");


        Authentication auth = new UsernamePasswordAuthenticationToken(
                mockUser.getEmail(),
                "password",
                List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);


        when(userRepository.findByEmail(mockUser.getEmail()))
                .thenReturn(Optional.of(mockUser));
    }

    @Test
    void shouldGetCustomerOrders() {

        Order order1 = OrderMockData.createOrder(mockUser);
        Order order2 = OrderMockData.createOrder(mockUser, LocalDate.now().minusDays(1));

        when(orderRepository.getAllUserOrders(mockUser.getId()))
                .thenReturn(Optional.of(List.of(order1, order2)));

        OrderResponseList responseList = orderQueryService.customerOrders();

        assertNotNull(responseList);
        assertEquals(2, responseList.list().size());
        verify(orderRepository).getAllUserOrders(mockUser.getId());
        verify(userRepository).findByEmail(mockUser.getEmail());
    }

    @Test
    void shouldGetRecentOrders() {
        Order order1 = OrderMockData.createOrder(mockUser);
        Order order2 = OrderMockData.createOrder(mockUser);

        when(orderRepository.findTop10ByOrderByOrderDateDesc())
                .thenReturn(Optional.of(List.of(order1, order2)));

        OrderResponseList responseList = orderQueryService.getRecentOrders();

        assertNotNull(responseList);
        assertEquals(2, responseList.list().size());
        verify(orderRepository).findTop10ByOrderByOrderDateDesc();
    }

    @Test
    void shouldCalculateTotalOrders() {

        List<Order> orders = List.of(
                OrderMockData.createOrder(mockUser),
                OrderMockData.createOrder(mockUser)
        );

        when(orderRepository.findAll()).thenReturn(orders);

        int totalOrders = orderQueryService.totalOrders();


        assertEquals(2, totalOrders);
        verify(orderRepository).findAll();
    }

    @Test
    void shouldCalculateTotalRevenue() {

        Order order1 = OrderMockData.createOrder(mockUser);
        order1.setAmount(100.0);
        Order order2 = OrderMockData.createOrder(mockUser);
        order2.setAmount(200.0);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        double totalRevenue = orderQueryService.totalRevenue();

        assertEquals(300.0, totalRevenue);
        verify(orderRepository).findAll();
    }

    @Test
    void shouldGetMonthlyRevenue() {
        Object[] monthData1 = new Object[]{"January", 1000.0};
        Object[] monthData2 = new Object[]{"February", 1500.0};

        when(orderRepository.getMonthlyRevenue())
                .thenReturn(List.of(monthData1, monthData2));

        Map<String, Double> monthlyRevenue = orderQueryService.getMonthlyRevenue();

        assertNotNull(monthlyRevenue);
        assertEquals(2, monthlyRevenue.size());
        assertEquals(1000.0, monthlyRevenue.get("January"));
        assertEquals(1500.0, monthlyRevenue.get("February"));
        verify(orderRepository).getMonthlyRevenue();
    }

    @Test
    void shouldGetAllOrders() {
        Order order1 = OrderMockData.createOrder(mockUser);
        Order order2 = OrderMockData.createOrder(mockUser);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        OrderResponseList responseList = orderQueryService.getAllOrders();

        assertNotNull(responseList);
        assertEquals(2, responseList.list().size());
        verify(orderRepository).findAll();
    }

    @Test
    void shouldThrowExceptionWhenUserNotAuthenticated() {
        SecurityContextHolder.clearContext();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NoUserFound.class, () -> orderQueryService.customerOrders());
    }
}