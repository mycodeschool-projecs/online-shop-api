package mycode.online_shop_api.app.orderDetails.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.products.model.Product;

import java.io.Serializable;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@Table(name = "order_details")
@Entity(name = "OrderDetails")
public class OrderDetails implements Serializable {

    @Id
    @SequenceGenerator(
            name = "order_details_sequence",
            sequenceName = "order_details_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "order_details_sequence"
    )
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @JsonBackReference
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonBackReference
    private Product product;

    @Column(
            name = "price",
            nullable = false,
            columnDefinition = "DOUBLE"
    )

    private double price;

    @Column(
            name = "quantity",
            nullable = false,
            columnDefinition = "INT"
    )
    private int quantity;



}
