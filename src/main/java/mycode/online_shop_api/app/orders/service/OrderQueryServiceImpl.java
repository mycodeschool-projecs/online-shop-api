package mycode.online_shop_api.app.orders.service;

import lombok.AllArgsConstructor;

import mycode.online_shop_api.app.orders.dtos.OrderResponse;
import mycode.online_shop_api.app.orders.dtos.OrderResponseList;
import mycode.online_shop_api.app.orders.exceptions.NoOrderFound;
import mycode.online_shop_api.app.orders.mappers.OrderMapper;
import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.orders.repository.OrderRepository;
import mycode.online_shop_api.app.users.exceptions.NoUserFound;
import mycode.online_shop_api.app.users.mapper.UserMapper;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService{


    private OrderRepository orderRepository;
    private UserRepository userRepository;


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NoUserFound("No authenticated user found");
        }

        String userEmail = authentication.getName();

        if (userEmail == null || userEmail.isEmpty()) {
            throw new NoUserFound("User email not found in authentication");
        }

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoUserFound("User not found for email: " + userEmail));
    }


    @Override
    public OrderResponseList customerOrders() {

        User user = getAuthenticatedUser();

        Optional<List<Order>> list = orderRepository.getAllUserOrders(user.getId());

        List<OrderResponse> rez = new ArrayList<>();

        list.ifPresent(orders -> orders.forEach(list1 -> {
            rez.add(OrderMapper.orderToResponseDto(list1));
        }));



        return new OrderResponseList(rez);

    }

    @Override
    public OrderResponseList getRecentOrders() {
        Optional<List<Order>> orders = orderRepository.findTop10ByOrderByOrderDateDesc();
        ArrayList<OrderResponse> responses = new ArrayList<>();
        orders.ifPresent(orderList -> orderList.forEach(order -> {
            responses.add(OrderMapper.orderToResponseDto(order));
        }));

        return new OrderResponseList(responses);
    }

    @Override
    public int totalOrders() {
        return orderRepository.findAll().size();
    }

    @Override
    public double totalRevenue() {
        List<Order> list = orderRepository.findAll();

        double sum =0;

        for (Order order : list) {
            sum += order.getAmount();
        }

        return sum;
    }

    @Override
    public Map<String, Double> getMonthlyRevenue() {
        List<Object[]> monthlyRevenueData = orderRepository.getMonthlyRevenue();
        Map<String, Double> monthlyRevenue = new HashMap<>();

        for (Object[] data : monthlyRevenueData) {
            String month = (String) data[0];
            Double revenue = (Double) data[1];
            monthlyRevenue.put(month, revenue);
        }

        return monthlyRevenue;
    }

    @Override
    public OrderResponseList getAllOrders() {
        List<Order> list = orderRepository.findAll();

        ArrayList<OrderResponse> responses = new ArrayList<>();

        list.forEach(order -> {
            responses.add(OrderMapper.orderToResponseDto(order));
        });

        return new OrderResponseList(responses);
    }

}
