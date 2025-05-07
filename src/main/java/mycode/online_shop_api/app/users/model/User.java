package mycode.online_shop_api.app.users.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.system.security.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = "cart")
@Table(name = "users")
@Entity(name = "User")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "user_sequence"
    )

    @Column(
            name = "id"
    )
    private long id;

    @JsonProperty("email")
    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String password;

    @JsonProperty("fullName")
    @Column(
            name = "full_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String fullName;

    @JsonProperty("phone")
    @Column(
            name = "phone",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String phone;

    @Column(
            name = "billing_address",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String billingAddress;

    @Column(
            name = "shipping_address",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String shippingAddress;

    @Column(
            name = "country",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String country;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy ="user",fetch = FetchType.LAZY,cascade = CascadeType.ALL ,orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonManagedReference
    private Set<Order> orders = new HashSet<>();

    public void addOrder(Order order){

        this.orders.add(order);
        order.setUser(this);

    }

    public void deleteOrder(Order order){
        this.orders.remove(order);
        order.setUser(null);
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonManagedReference
    private Cart cart;

    public void setCart(Cart cart) {
        this.cart = cart;
        cart.setUser(this);
    }

    public User(Long id, String fullName, String phoneNumber, String email, String password, UserRole userRole, String shippingAddress, String billingAddress) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phoneNumber;
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.userRole = userRole;
        this.billingAddress= billingAddress;
        this.shippingAddress = shippingAddress;

    }


    public void setPassword(String password){
        this.password= new BCryptPasswordEncoder().encode(password);
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRole.getGrantedAuthorities();
    }

    @Override
    public String getUsername(){
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
