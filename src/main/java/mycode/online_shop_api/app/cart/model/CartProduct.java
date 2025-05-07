package mycode.online_shop_api.app.cart.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import mycode.online_shop_api.app.products.model.Product;


import java.util.Objects;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@Entity
@Table(name = "cart_products")
public class CartProduct {

    @Id
    @SequenceGenerator(
            name = "cart_product_sequence",
            sequenceName = "cart_product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "cart_product_sequence"
    )
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false)
    private Cart cart;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartProduct cartProduct = (CartProduct) obj;
        return Objects.equals(id, cartProduct.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
