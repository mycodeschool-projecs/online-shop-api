package mycode.online_shop_api.app.orders.dtos;

public record EditOrderRequest(String action, String productName, Integer quantity) {
}
