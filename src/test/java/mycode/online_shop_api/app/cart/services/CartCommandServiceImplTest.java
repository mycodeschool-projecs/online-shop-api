package mycode.online_shop_api.app.cart.services;

import mycode.online_shop_api.app.cart.dtos.AddProductToCartRequest;
import mycode.online_shop_api.app.cart.dtos.UpdateCartQuantityRequest;
import mycode.online_shop_api.app.cart.exceptions.NoCartFound;
import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.cart.model.CartProduct;
import mycode.online_shop_api.app.cart.repository.CartProductRepository;
import mycode.online_shop_api.app.cart.repository.CartRepository;
import mycode.online_shop_api.app.products.exceptions.NoProductFound;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.repository.ProductRepository;
import mycode.online_shop_api.app.users.exceptions.NoUserFound;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartCommandServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartProductRepository cartProductRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CartCommandServiceImpl cartCommandService;

    private User mockUser;
    private Product mockProduct;
    private Cart mockCart;
    private CartProduct mockCartProduct;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("test@example.com");

        mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setName("Test Product");

        mockCart = new Cart();
        mockCart.setId(1L);
        mockCart.setUser(mockUser);

        mockCartProduct = new CartProduct();
        mockCartProduct.setCart(mockCart);
        mockCartProduct.setProduct(mockProduct);
        mockCartProduct.setQuantity(2);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
    }

    @Test
    void shouldAddProductToCartSuccessfully() {
        AddProductToCartRequest request = new AddProductToCartRequest(1, 2);

        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(cartRepository.save(any())).thenReturn(mockCart);

        var response = cartCommandService.addProductToCart(request);

        assertEquals(1L, response.userId());
        verify(cartRepository).save(any());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        AddProductToCartRequest request = new AddProductToCartRequest(1, 2);

        assertThrows(NoUserFound.class, () -> cartCommandService.addProductToCart(request));
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        AddProductToCartRequest request = new AddProductToCartRequest(1, 2);
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoProductFound.class, () -> cartCommandService.addProductToCart(request));
    }

    @Test
    void shouldThrowWhenCartNotFoundForDelete() {
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.empty());

        assertThrows(NoCartFound.class, () -> cartCommandService.deleteProductFromCart(1));
    }

    @Test
    void shouldDeleteProductFromCartSuccessfully() {
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        mockCart.setCartProducts(new HashSet<>(List.of(mockCartProduct)));

        var response = cartCommandService.deleteProductFromCart(1);

        assertNotNull(response);
        verify(cartRepository).save(mockCart);
    }

    @Test
    void shouldThrowWhenProductIsNotInCart() {
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        mockCart.setCartProducts(new HashSet<>());

        assertThrows(NoCartFound.class, () -> cartCommandService.deleteProductFromCart(1));
    }

    @Test
    void shouldThrowWhenDifferentProductInCart() {
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        Product other = new Product();
        other.setId(2);
        CartProduct otherCartProduct = new CartProduct();
        otherCartProduct.setProduct(other);
        mockCart.setCartProducts(new HashSet<>(List.of(otherCartProduct)));

        assertThrows(NoProductFound.class, () -> cartCommandService.deleteProductFromCart(1));
    }

    @Test
    void shouldEmptyUserCartSuccessfully() {
        List<CartProduct> cartProducts = List.of(mockCartProduct);
        mockCart.setCartProducts(new HashSet<>(cartProducts));

        mockCart.setCartProducts(new HashSet<>(cartProducts));
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockCart));

        String response = cartCommandService.emptyUserCart();

        assertEquals("Cart for user with Id: 1 deleted", response);
        verify(cartProductRepository).deleteAll(cartProducts);
        verify(cartRepository).save(mockCart);
    }

    @Test
    void shouldThrowWhenNoCartFoundForUser() {
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.empty());

        assertThrows(NoCartFound.class, () -> cartCommandService.emptyUserCart());
    }

    @Test
    void shouldThrowWhenNoProductsFoundInCart() {
        mockCart.setCartProducts(new HashSet<>());
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockCart));

        assertThrows(NoCartFound.class, () -> cartCommandService.emptyUserCart());
    }

    @Test
    void shouldUpdateCartQuantitySuccessfully() {
        UpdateCartQuantityRequest request = new UpdateCartQuantityRequest(5);

        mockCart.setCartProducts(new HashSet<>(List.of(mockCartProduct)));
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        var response = cartCommandService.updateCartQuantity(request, 1);

        assertNotNull(response);
        verify(cartProductRepository).save(mockCartProduct);
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentProductInCart() {
        UpdateCartQuantityRequest request = new UpdateCartQuantityRequest(5);
        CartProduct otherCartProduct = new CartProduct();
        Product other = new Product();
        other.setId(2);
        otherCartProduct.setProduct(other);
        mockCart.setCartProducts(new HashSet<>(List.of(otherCartProduct)));

        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        assertThrows(NoProductFound.class, () -> cartCommandService.updateCartQuantity(request, 1));
    }
}
