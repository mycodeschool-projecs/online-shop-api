package mycode.online_shop_api.app.orders.repository;



import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.users.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @EntityGraph(attributePaths = {"orderDetails"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT co FROM Order co LEFT JOIN co.user c WHERE c.id = :userId")
    Optional<List<Order>> getAllUserOrders(long userId);


    Optional<List<Order>> findTop10ByOrderByOrderDateDesc();

    @Query("SELECT o.user FROM Order o GROUP BY o.user ORDER BY COUNT(o.id) DESC")
    Optional<List<User>> findMostActiveUsers();

    @Query(value = """
    WITH months AS (
        SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL n MONTH), '%Y-%m') AS month
        FROM (
            SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
            UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
            UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11
        ) AS numbers
    )
    SELECT 
        m.month,
        COALESCE(SUM(o.amount), 0) AS revenue
    FROM 
        months m
    LEFT JOIN 
        customer_order o ON DATE_FORMAT(o.order_date, '%Y-%m') = m.month
    GROUP BY 
        m.month
    ORDER BY 
        m.month
""", nativeQuery = true)
    List<Object[]> getMonthlyRevenue();

}
