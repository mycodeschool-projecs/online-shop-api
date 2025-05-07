package mycode.online_shop_api.app.orderDetails.mock;

import mycode.online_shop_api.app.orderDetails.model.OrderDetails;
import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.mocks.ProductMockData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsMockData {

    public static OrderDetails createOrderDetails(Order order, Product product, double price, int quantity) {
        return OrderDetails.builder()
                .order(order)
                .product(product)
                .price(price)
                .quantity(quantity)
                .build();
    }

    public static List<OrderDetails> createSampleOrderDetails(Order order) {
        List<OrderDetails> orderDetails = new ArrayList<>();


        Product cheapPhone = ProductMockData.createProduct("Cheap Phone", 100, 10, 0.5, "Basic phone", "Phone", LocalDate.now());
        Product midPhone = ProductMockData.createProduct("Mid Phone", 500, 5, 0.8, "Mid-range phone", "Phone", LocalDate.now());
        Product expensivePhone = ProductMockData.createProduct("Expensive Phone", 1500, 2, 1.0, "High-end phone", "Phone", LocalDate.now());


        orderDetails.add(createOrderDetails(order, cheapPhone, 100, 2));
        orderDetails.add(createOrderDetails(order, midPhone, 500, 1));
        orderDetails.add(createOrderDetails(order, expensivePhone, 1500, 1));

        return orderDetails;
    }

    public static OrderDetails createSingleOrderDetail(Order order, Product product) {
        return createOrderDetails(order, product, product.getPrice(), 1);
    }
}