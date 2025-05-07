package mycode.online_shop_api.app.orderDetails.dtos;


import jakarta.validation.constraints.NotNull;
import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.products.model.Product;



public record CreateOrderDetailsRequest(
        @NotNull
        double price,
        @NotNull
        int quantity,
        @NotNull
        Order order,
        @NotNull
        Product product){}

