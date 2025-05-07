package mycode.online_shop_api.app.orderDetails.repository;

import mycode.online_shop_api.app.orderDetails.mock.OrderDetailsMockData;
import mycode.online_shop_api.app.orderDetails.model.OrderDetails;
import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.orders.mock.OrderMockData;
import mycode.online_shop_api.app.orders.repository.OrderRepository;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.mocks.ProductMockData;
import mycode.online_shop_api.app.products.repository.ProductRepository;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.mock.UserMockData;
import mycode.online_shop_api.app.users.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class OrderDetailsRepositoryTest {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private Order testOrder;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        User testUser = UserMockData.createUser("test@example.com", "Test User");
        userRepository.save(testUser);

        testProduct = ProductMockData.createProduct(
                "Test Product",
                100.0,
                10,
                0.5,
                "Test Description",
                "Test Category",
                LocalDate.now()
        );
        productRepository.save(testProduct);

        testOrder = OrderMockData.createOrder(testUser);
        orderRepository.save(testOrder);

        OrderDetails orderDetails = OrderDetailsMockData.createSingleOrderDetail(testOrder, testProduct);
        orderDetailsRepository.save(orderDetails);
    }

    @Test
    void shouldFindOrderDetailsByOrderIdAndProductId() {
        Optional<OrderDetails> foundOrderDetails = orderDetailsRepository.findByOrderIdAndProductId(
                testOrder.getId(),
                testProduct.getId()
        );

        assertTrue(foundOrderDetails.isPresent());
        assertEquals(testProduct.getId(), foundOrderDetails.get().getProduct().getId());
        assertEquals(testOrder.getId(), foundOrderDetails.get().getOrder().getId());
    }

    @Test
    void shouldReturnEmptyWhenOrderDetailsNotFound() {
        Optional<OrderDetails> foundOrderDetails = orderDetailsRepository.findByOrderIdAndProductId(999, 999);

        assertFalse(foundOrderDetails.isPresent());
    }

    @Test
    void shouldFindMostSoldProducts() {
        Product product1 = ProductMockData.createProduct("Product 1", 50.0, 20, 0.3, "Description 1", "Category", LocalDate.now());
        Product product2 = ProductMockData.createProduct("Product 2", 75.0, 15, 0.4, "Description 2", "Category", LocalDate.now());
        productRepository.save(product1);
        productRepository.save(product2);

        User user1 = UserMockData.createUser("user1@example.com", "User 1");
        User user2 = UserMockData.createUser("user2@example.com", "User 2");
        userRepository.save(user1);
        userRepository.save(user2);

        Order order1 = OrderMockData.createOrder(user1);
        Order order2 = OrderMockData.createOrder(user2);
        orderRepository.save(order1);
        orderRepository.save(order2);

        OrderDetails orderDetails1 = OrderDetailsMockData.createOrderDetails(order1, product1, 50.0, 5);
        OrderDetails orderDetails2 = OrderDetailsMockData.createOrderDetails(order2, product1, 50.0, 3);
        OrderDetails orderDetails3 = OrderDetailsMockData.createOrderDetails(order1, product2, 75.0, 2);
        orderDetailsRepository.saveAll(List.of(orderDetails1, orderDetails2, orderDetails3));

        List<Integer> mostSoldProductIds = orderDetailsRepository.mostSoldProduct();

        assertFalse(mostSoldProductIds.isEmpty());
        assertEquals(product1.getId(), mostSoldProductIds.get(0));
    }

    @Test
    void shouldFindTopSellingProducts() {
        Product product1 = ProductMockData.createProduct("Product 1", 50.0, 20, 0.3, "Description 1", "Category", LocalDate.now());
        Product product2 = ProductMockData.createProduct("Product 2", 75.0, 15, 0.4, "Description 2", "Category", LocalDate.now());
        productRepository.save(product1);
        productRepository.save(product2);

        User user1 = UserMockData.createUser("user1@example.com", "User 1");
        User user2 = UserMockData.createUser("user2@example.com", "User 2");
        userRepository.save(user1);
        userRepository.save(user2);

        Order order1 = OrderMockData.createOrder(user1);
        Order order2 = OrderMockData.createOrder(user2);
        orderRepository.save(order1);
        orderRepository.save(order2);

        OrderDetails orderDetails1 = OrderDetailsMockData.createOrderDetails(order1, product1, 50.0, 5);
        OrderDetails orderDetails2 = OrderDetailsMockData.createOrderDetails(order2, product1, 50.0, 3);
        OrderDetails orderDetails3 = OrderDetailsMockData.createOrderDetails(order1, product2, 75.0, 2);
        orderDetailsRepository.saveAll(List.of(orderDetails1, orderDetails2, orderDetails3));

        List<Product> topSellingProducts = orderDetailsRepository.findTopSellingProducts();

        assertFalse(topSellingProducts.isEmpty());
        assertEquals(product1.getId(), topSellingProducts.get(0).getId());
    }
}