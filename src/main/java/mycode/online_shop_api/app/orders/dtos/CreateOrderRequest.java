package mycode.online_shop_api.app.orders.dtos;


import jakarta.validation.constraints.NotNull;
import mycode.online_shop_api.app.cart.dtos.AddProductToCartRequest;




import java.time.LocalDate;
import java.util.List;


public record CreateOrderRequest(
        @NotNull
        LocalDate orderDate,
        @NotNull
        List<AddProductToCartRequest> productList){


}
