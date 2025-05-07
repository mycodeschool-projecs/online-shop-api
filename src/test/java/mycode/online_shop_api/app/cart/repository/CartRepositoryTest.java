package mycode.online_shop_api.app.cart.repository;

import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.repository.UserRepository;
import mycode.online_shop_api.app.system.security.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Cart testCart;

    @BeforeEach
    void setUp() {

        cartRepository.deleteAll();
        userRepository.deleteAll();


        testUser = User.builder()
                .email("test@example.com")
                .fullName("Test User")
                .password("password")
                .phone("1234567890")
                .userRole(UserRole.CLIENT)
                .billingAddress("Billing Address")
                .country("Test Country")
                .shippingAddress("Shipping Address")
                .build();
        testUser = userRepository.save(testUser);


        testCart = new Cart();
        testCart.setUser(testUser);
        testCart = cartRepository.save(testCart);
    }

    @Test
    @DisplayName("Should find cart by user ID")
    void testFindByUserId() {
        Optional<Cart> foundCart = cartRepository.findByUserId(testUser.getId());

        assertTrue(foundCart.isPresent(), "Cart should be found for user ID");
        assertEquals(testCart.getId(), foundCart.get().getId(), "Cart ID should match");
    }

    @Test
    @DisplayName("Should return empty when user has no cart")
    void testFindByUserId_NotFound() {
        Optional<Cart> foundCart = cartRepository.findByUserId(999L);
        assertTrue(foundCart.isEmpty(), "Should return empty for a non-existing user ID");
    }
}