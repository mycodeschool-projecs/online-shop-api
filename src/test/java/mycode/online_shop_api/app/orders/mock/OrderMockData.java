package mycode.online_shop_api.app.orders.mock;

import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.users.model.User;

import java.time.LocalDate;

public class OrderMockData {

    public static Order createOrder(User user) {
        return createOrder(user, LocalDate.now());
    }

    public static Order createOrder(User user, LocalDate orderDate) {
        return Order.builder()
                .user(user)
                .orderDate(orderDate != null ? orderDate : LocalDate.now())
                .orderStatus("PENDING")
                .orderEmail(user != null ? user.getEmail() : "test@example.com")
                .amount(0.0)
                .orderAddress("Test Address")
                .shippingAddress("Test Shipping Address")
                .build();
    }
}