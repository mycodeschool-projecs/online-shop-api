package mycode.online_shop_api.app.cart.services;

import mycode.online_shop_api.app.cart.dtos.AddProductToCartRequest;
import mycode.online_shop_api.app.cart.dtos.CartResponse;
import mycode.online_shop_api.app.cart.dtos.UpdateCartQuantityRequest;

public interface CartCommandService {

    CartResponse addProductToCart(AddProductToCartRequest cartRequest);

    CartResponse deleteProductFromCart(int productId);

    CartResponse updateCartQuantity(UpdateCartQuantityRequest updateCartQuantityRequest, int productId);

    String emptyUserCart();

}
