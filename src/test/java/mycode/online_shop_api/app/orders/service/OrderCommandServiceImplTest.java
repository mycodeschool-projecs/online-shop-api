package mycode.online_shop_api.app.orders.service;

import mycode.online_shop_api.app.cart.dtos.AddProductToCartRequest;
import mycode.online_shop_api.app.orderDetails.model.OrderDetails;
import mycode.online_shop_api.app.orderDetails.repository.OrderDetailsRepository;
import mycode.online_shop_api.app.orders.dtos.CreateOrderRequest;
import mycode.online_shop_api.app.orders.dtos.CreateOrderUpdateRequest;
import mycode.online_shop_api.app.orders.dtos.EditOrderRequest;
import mycode.online_shop_api.app.orders.dtos.OrderResponse;
import mycode.online_shop_api.app.orders.exceptions.NoOrderFound;
import mycode.online_shop_api.app.orders.mock.OrderMockData;
import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.orders.repository.OrderRepository;
import mycode.online_shop_api.app.products.exceptions.NoProductFound;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.repository.ProductRepository;
import mycode.online_shop_api.app.users.mock.UserMockData;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderCommandServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderCommandServiceImpl orderCommandService;

    private User mockUser;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setBillingAddress("Test Billing Address");
        mockUser.setShippingAddress("Test Shipping Address");


        mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setName("Test Product");
        mockProduct.setPrice(100.0);

        Authentication auth = new UsernamePasswordAuthenticationToken(mockUser.getEmail(), "password");
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));
    }

    @Test
    void shouldAddOrderSuccessfully() {
        AddProductToCartRequest cartRequest = new AddProductToCartRequest(1, 2);
        CreateOrderRequest orderRequest = new CreateOrderRequest(LocalDate.now(),List.of(cartRequest));

        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        when(orderRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderDetailsRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderCommandService.addOrder(orderRequest);

        assertNotNull(response);
        assertEquals(mockUser.getEmail(), response.orderEmail());
        assertEquals(200.0, response.amount());
        assertEquals("PREPARING", response.orderStatus());

        verify(orderRepository, times(2)).saveAndFlush(any());
        verify(orderDetailsRepository).saveAndFlush(any());
    }



    @Test
    void shouldThrowExceptionWhenAddingOrderWithEmptyCart() {
        CreateOrderRequest emptyOrderRequest = new CreateOrderRequest(LocalDate.now(),List.of());

        assertThrows(NoProductFound.class, () -> orderCommandService.addOrder(emptyOrderRequest));
    }

    @Test
    void shouldDeleteOrderSuccessfully() {

        Order order = OrderMockData.createOrder(mockUser);
        order.setId(1);


        when(orderRepository.findById(1)).thenReturn(Optional.of(order));


        OrderResponse response = orderCommandService.deleteOrder(1);


        assertNotNull(response);
        assertEquals(order.getOrderEmail(), response.orderEmail());


        verify(orderRepository).delete(order);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentOrder() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NoOrderFound.class, () -> orderCommandService.deleteOrder(1));
    }

    @Test
    void shouldUpdateOrderSuccessfully() {
        int orderId = 1;

        User mockUser = UserMockData.createUser("test@example.com", "Test User");
        mockUser.setId(1);

        Order existingOrder = OrderMockData.createOrder(mockUser);
        existingOrder.setId(orderId);

        CreateOrderUpdateRequest updateRequest = new CreateOrderUpdateRequest(
                "new@example.com", "New Shipping Address", "New Address", 200, "SHIPPED"
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.saveAndFlush(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderCommandService.updateOrder(orderId, updateRequest);

        assertNotNull(response);
        assertEquals(200.0, response.amount());
        assertEquals("new@example.com", response.orderEmail());
        assertEquals("SHIPPED", response.orderStatus());
        assertEquals("New Address", response.orderAddress());
        assertEquals("New Shipping Address", response.shippingAddress());

        verify(orderRepository).saveAndFlush(existingOrder);
    }


    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentOrder() {
        int orderId = 999;
        CreateOrderUpdateRequest updateRequest = new CreateOrderUpdateRequest(
                "new@example.com", "New Shipping Address", "New Address", 200, "SHIPPED"
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(NoOrderFound.class, () -> orderCommandService.updateOrder(orderId, updateRequest));
        verify(orderRepository, never()).saveAndFlush(any(Order.class));
    }



    @Test
    void shouldCancelOrderSuccessfully() {

        Order order = OrderMockData.createOrder(mockUser);
        order.setId(1);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrder(order);
        orderDetails.setProduct(mockProduct);

        order.addOrderDetails(orderDetails);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        OrderResponse response = orderCommandService.cancelOrder(1);

        assertNotNull(response);
        assertEquals("CANCELLED", response.orderStatus());

        verify(orderDetailsRepository, times(1)).delete(orderDetails);
        verify(orderRepository).save(order);
    }
}