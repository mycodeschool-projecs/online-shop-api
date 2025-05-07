package mycode.online_shop_api.app.orders.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import mycode.online_shop_api.app.orderDetails.model.OrderDetails;
import mycode.online_shop_api.app.users.model.User;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString(exclude = "orderDetails")
@Entity
@Table(name = "customer_order")
public class Order{

    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "order_sequence"
    )
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "shipping_address", nullable = false, columnDefinition = "TEXT")
    private String shippingAddress;

    @Column(name = "order_address", nullable = false, columnDefinition = "TEXT")
    private String orderAddress;

    @Column(name = "order_email", nullable = false, columnDefinition = "TEXT")
    private String orderEmail;

    @Column(name = "order_date", nullable = false, columnDefinition = "DATE")
    private LocalDate orderDate;

    @Column(name = "order_status", nullable = false, columnDefinition = "TEXT")
    private String orderStatus;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private Set<OrderDetails> orderDetails = new HashSet<>();

    public void addOrderDetails(OrderDetails orderDetails) {
        this.orderDetails.add(orderDetails);
        orderDetails.setOrder(this);
    }

    public void removeOrderDetails(OrderDetails orderDetails) {
        this.orderDetails.remove(orderDetails);
        orderDetails.setOrder(null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(user, order.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}
