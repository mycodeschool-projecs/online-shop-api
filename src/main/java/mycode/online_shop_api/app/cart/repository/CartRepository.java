package mycode.online_shop_api.app.cart.repository;

import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.cart.model.CartProduct;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.users.model.User;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.awt.font.OpenType;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUserId(long userId);


}
