package mycode.online_shop_api.app.cart.repository;

import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.cart.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {


    Optional<List<CartProduct>> getAllByCart(Cart cart);
}
