package mycode.online_shop_api.app.cart.mapper;

import lombok.AllArgsConstructor;
import mycode.online_shop_api.app.cart.dtos.CartProductResponse;
import mycode.online_shop_api.app.cart.dtos.CartResponse;
import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.products.dto.ProductResponse;
import mycode.online_shop_api.app.products.dto.ProductResponseList;
import mycode.online_shop_api.app.products.mapper.ProductMapper;
import mycode.online_shop_api.app.products.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;


public class CartMapper{


    public static CartResponse cartToResponseDto(Cart cart){


        List<CartProductResponse> productResponses = cart.getCartProducts().stream()
                .map(cartProduct -> new CartProductResponse(
                        cartProduct.getProduct().getId(),
                        cartProduct.getProduct().getName(),
                        cartProduct.getProduct().getCategory(),
                        cartProduct.getProduct().getPrice(),
                        cartProduct.getQuantity()
                ))
                .toList();

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .list(productResponses).build();

    }
}
