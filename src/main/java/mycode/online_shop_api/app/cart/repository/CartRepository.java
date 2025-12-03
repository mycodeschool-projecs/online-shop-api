package mycode.online_shop_api.app.cart.repository;

import mycode.online_shop_api.app.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(long userId);
}
