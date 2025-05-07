package mycode.online_shop_api.app.cart.services;

import mycode.online_shop_api.app.cart.dtos.CartResponse;
import mycode.online_shop_api.app.cart.exceptions.NoCartFound;
import mycode.online_shop_api.app.cart.mapper.CartMapper;
import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.cart.repository.CartRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartQueryServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CartQueryServiceImpl cartQueryService;

    private User mockUser;
    private Cart mockCart;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("test@example.com");

        mockCart = new Cart();
        mockCart.setId(1L);
        mockCart.setUser(mockUser);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
    }

    @Test
    void shouldReturnCartSuccessfully() {
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockCart));

        CartResponse response = cartQueryService.getCart();

        assertNotNull(response);
        verify(cartRepository).findByUserId(mockUser.getId());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(NoUserFound.class, () -> cartQueryService.getCart());
    }

    @Test
    void shouldThrowWhenCartNotFound() {
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.empty());

        assertThrows(NoCartFound.class, () -> cartQueryService.getCart());
    }
}
