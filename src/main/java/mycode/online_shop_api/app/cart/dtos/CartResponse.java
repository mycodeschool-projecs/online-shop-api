package mycode.online_shop_api.app.cart.dtos;

import lombok.Builder;
import mycode.online_shop_api.app.products.dto.ProductResponseList;

import java.util.List;

@Builder

public record CartResponse(long id, long userId, List<CartProductResponse> list) {
}
