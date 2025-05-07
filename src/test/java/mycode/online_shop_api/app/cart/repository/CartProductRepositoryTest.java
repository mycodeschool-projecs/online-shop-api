package mycode.online_shop_api.app.cart.repository;

import mycode.online_shop_api.app.cart.mock.CartMockData;
import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.cart.model.CartProduct;
import mycode.online_shop_api.app.products.mocks.ProductMockData;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.repository.ProductRepository;
import mycode.online_shop_api.app.system.security.UserRole;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class CartProductRepositoryTest {

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private Cart testCart;

    @BeforeEach
    void setUp() {

        cartProductRepository.deleteAll();
        productRepository.deleteAll();
        cartRepository.deleteAll();
        userRepository.deleteAll();


        User testUser = User.builder()
                .email("test@example.com")
                .fullName("Test User")
                .password("password")
                .phone("1234567890")
                .userRole(UserRole.ADMIN)
                .billingAddress("Test Billing Address")
                .country("Test Country")
                .shippingAddress("Test Shipping Address")
                .build();
        userRepository.save(testUser);


        testCart = new Cart();
        testCart.setUser(testUser);
        testCart = cartRepository.save(testCart);

        Product cheapPhone = ProductMockData.createProduct(
                "Cheap Phone", 100, 10, 0.5,
                "Basic phone", "Phone", java.time.LocalDate.now());
        Product midPhone = ProductMockData.createProduct(
                "Mid Phone", 500, 5, 0.8,
                "Mid-range phone", "Phone", java.time.LocalDate.now());
        cheapPhone = productRepository.save(cheapPhone);
        midPhone = productRepository.save(midPhone);


        CartProduct cheapPhoneCartProduct = new CartProduct();
        cheapPhoneCartProduct.setCart(testCart);
        cheapPhoneCartProduct.setProduct(cheapPhone);
        cheapPhoneCartProduct.setQuantity(2);
        cartProductRepository.save(cheapPhoneCartProduct);

        CartProduct midPhoneCartProduct = new CartProduct();
        midPhoneCartProduct.setCart(testCart);
        midPhoneCartProduct.setProduct(midPhone);
        midPhoneCartProduct.setQuantity(1);
        cartProductRepository.save(midPhoneCartProduct);


        testCart = cartRepository.findById(Math.toIntExact(testCart.getId())).orElseThrow();
    }

    @Test
    @DisplayName("Should find all cart products by cart")
    void testGetAllByCart() {

        List<CartProduct> cartProducts = testCart.getCartProducts().stream().toList();


        Optional<List<CartProduct>> result = cartProductRepository.getAllByCart(testCart);


        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertTrue(result.get().stream()
                .anyMatch(cp -> cp.getProduct().getName().equals("Cheap Phone") && cp.getQuantity() == 2));
        assertTrue(result.get().stream()
                .anyMatch(cp -> cp.getProduct().getName().equals("Mid Phone") && cp.getQuantity() == 1));
    }

    @Test
    @DisplayName("Should return empty list when cart has no products")
    void testGetAllByCartEmpty() {

        cartProductRepository.deleteAll();
        testCart.getCartProducts().clear();
        cartRepository.save(testCart);

        Optional<List<CartProduct>> result = cartProductRepository.getAllByCart(testCart);

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }
    
}