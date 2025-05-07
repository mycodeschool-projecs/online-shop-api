package mycode.online_shop_api.app.cart.mock;

import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.cart.model.CartProduct;
import mycode.online_shop_api.app.products.mocks.ProductMockData;
import mycode.online_shop_api.app.products.model.Product;

public class CartProductMockData {

    public static CartProduct createCartProduct(Cart cart, Product product, int quantity) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(quantity);
        return cartProduct;
    }

    public static CartProduct createSampleCartProduct() {
        Cart cart = new Cart();
        Product product = ProductMockData.createProduct("Sample Product", 200, 5, 1.2, "Sample Description", "Sample Category", java.time.LocalDate.now());
        return createCartProduct(cart, product, 2);
    }
}
