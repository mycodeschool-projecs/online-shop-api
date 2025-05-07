package mycode.online_shop_api.app.cart.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddProductToCartRequest(
        @NotNull int productId,
        @NotNull int quantity
) {
}
