package mycode.online_shop_api.app.orders.service;

import mycode.online_shop_api.app.orders.dtos.OrderResponse;
import mycode.online_shop_api.app.orders.dtos.OrderResponseList;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface OrderQueryService {

    OrderResponseList customerOrders();

    OrderResponseList getRecentOrders();

    int totalOrders();

    double totalRevenue();

    Map<String, Double> getMonthlyRevenue();

    OrderResponseList getAllOrders();
}
