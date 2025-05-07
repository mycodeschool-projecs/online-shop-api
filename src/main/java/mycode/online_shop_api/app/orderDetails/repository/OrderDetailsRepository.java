package mycode.online_shop_api.app.orderDetails.repository;



import mycode.online_shop_api.app.orderDetails.model.OrderDetails;
import mycode.online_shop_api.app.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {


        @Query("SELECT od.product.id, COUNT(od) AS sales FROM OrderDetails od GROUP BY od.product.id ORDER BY sales DESC")
        List<Integer> mostSoldProduct();

        Optional<OrderDetails> findByOrderIdAndProductId(int orderId, int productId);

        @Query("SELECT oi.product FROM OrderDetails oi GROUP BY oi.product ORDER BY SUM(oi.quantity) DESC")
        List<Product> findTopSellingProducts();

}
