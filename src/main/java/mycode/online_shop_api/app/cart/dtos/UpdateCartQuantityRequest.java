package mycode.online_shop_api.app.cart.dtos;

import jakarta.validation.constraints.NotNull;


public record UpdateCartQuantityRequest(@NotNull int quantity) {
}
