package mycode.online_shop_api.app.orders.repository;

import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.mock.UserMockData;
import mycode.online_shop_api.app.orders.mock.OrderMockData;

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
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager entityManager;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {

        testUser1 = UserMockData.createUser("user1@example.com", "Test User 1");
        testUser2 = UserMockData.createUser("user2@example.com", "Test User 2");
        entityManager.persist(testUser1);
        entityManager.persist(testUser2);


        for (int i = 0; i < 5; i++) {
            Order order1 = OrderMockData.createOrder(testUser1);
            order1.setOrderDate(LocalDate.now().minusMonths(i));
            order1.setAmount(100.0 * (i + 1));
            entityManager.persist(order1);

            Order order2 = OrderMockData.createOrder(testUser2);
            order2.setOrderDate(LocalDate.now().minusMonths(i + 1));
            order2.setAmount(150.0 * (i + 1));
            entityManager.persist(order2);
        }
        entityManager.flush();
    }

    @Test
    void shouldFindAllUserOrders() {
        Optional<List<Order>> userOrders = orderRepository.getAllUserOrders(testUser1.getId());

        assertTrue(userOrders.isPresent());
        assertEquals(5, userOrders.get().size());
        assertTrue(userOrders.get().stream().allMatch(order -> order.getUser().equals(testUser1)));
    }

    @Test
    void shouldFindTop10RecentOrders() {
        Optional<List<Order>> recentOrders = orderRepository.findTop10ByOrderByOrderDateDesc();

        assertTrue(recentOrders.isPresent());
        assertTrue(recentOrders.get().size() <= 10);


        for (int i = 1; i < recentOrders.get().size(); i++) {
            LocalDate previousOrderDate = recentOrders.get().get(i-1).getOrderDate();
            LocalDate currentOrderDate = recentOrders.get().get(i).getOrderDate();

            assertTrue(
                    previousOrderDate.isAfter(currentOrderDate) ||
                            previousOrderDate.isEqual(currentOrderDate),
                    "Orders should be in non-increasing order of date"
            );
        }
    }

    @Test
    void shouldFindMostActiveUsers() {
        Optional<List<User>> mostActiveUsers = orderRepository.findMostActiveUsers();

        assertTrue(mostActiveUsers.isPresent());
        assertFalse(mostActiveUsers.get().isEmpty());

        assertTrue(
                mostActiveUsers.get().contains(testUser1) ||
                        mostActiveUsers.get().contains(testUser2)
        );
    }

}