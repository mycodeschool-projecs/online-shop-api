package mycode.online_shop_api.app.cart.repository;

import mycode.online_shop_api.app.cart.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
}
