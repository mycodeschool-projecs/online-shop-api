package mycode.online_shop_api.app.cart.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.users.model.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = "user")
@Table(name = "cart")
@Entity(name = "Cart")
public class Cart {

    @Id
    @SequenceGenerator(
            name = "cart_sequence",
            sequenceName = "cart_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "cart_sequence"
    )
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private User user;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CartProduct> cartProducts = new HashSet<>();


    public void addProduct(Product product, int quantity) {

        Optional<CartProduct> existingCartProduct = this.cartProducts.stream()
                .filter(cartProduct -> cartProduct.getProduct().equals(product))
                .findFirst();

        if (existingCartProduct.isPresent()) {

            CartProduct cartProduct = existingCartProduct.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
        } else {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setCart(this);
            cartProduct.setProduct(product);
            cartProduct.setQuantity(quantity);
            this.cartProducts.add(cartProduct);
        }
    }

    public void removeProduct(Product product) {
        this.cartProducts.removeIf(cartProduct -> cartProduct.getProduct().equals(product));
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cart cart = (Cart) obj;
        return Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
