package mycode.online_shop_api.app.cart.services;


import lombok.AllArgsConstructor;
import mycode.online_shop_api.app.cart.dtos.CartResponse;
import mycode.online_shop_api.app.cart.exceptions.NoCartFound;
import mycode.online_shop_api.app.cart.mapper.CartMapper;
import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.cart.repository.CartRepository;
import mycode.online_shop_api.app.users.exceptions.NoUserFound;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartQueryServiceImpl implements CartQueryService{

    private CartRepository cartRepository;
    private UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoUserFound("User not found"));
    }

    @Override
    public CartResponse getCart() {
        User user = getAuthenticatedUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoCartFound("No cart with this user id found"));

        return CartMapper.cartToResponseDto(cart);
    }
}


