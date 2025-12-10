package mycode.online_shop_api.app.users.service;


import lombok.AllArgsConstructor;
import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.cart.repository.CartRepository;
import mycode.online_shop_api.app.system.security.UserRole;
import mycode.online_shop_api.app.users.dtos.CreateUserRequest;
import mycode.online_shop_api.app.users.dtos.UpdateUserRequest;
import mycode.online_shop_api.app.users.dtos.UserResponse;
import mycode.online_shop_api.app.users.exceptions.NoUserFound;
import mycode.online_shop_api.app.users.exceptions.UserAlreadyExists;
import mycode.online_shop_api.app.users.mapper.UserMapper;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;

@AllArgsConstructor
@Service
public class UserCommandServiceImpl implements UserCommandService{

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private CartRepository cartRepository;


    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {

        UserRole role = resolveRequestedRole(createUserRequest);

        if (userRepository.existsByEmail(createUserRequest.email())) {
            throw new UserAlreadyExists("User with this email already exists");
        }

        User user  = User.builder()
                .phone(createUserRequest.phone())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .fullName(createUserRequest.fullName())
                .email(createUserRequest.email())
                .userRole(role)
                .billingAddress(createUserRequest.billingAddress())
                .shippingAddress(createUserRequest.shippingAddress())
                .country(createUserRequest.country())
                .build();

        userRepository.saveAndFlush(user);
        Cart cart = Cart.builder().user(user).build();
        cartRepository.saveAndFlush(cart);


        return UserMapper.userToResponseDto(user);
    }

    @Override
    public UserResponse deleteUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoUserFound("No user with this id found"));


        UserResponse response = UserMapper.userToResponseDto(user);

        userRepository.delete(user);

        return response;
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest up, long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoUserFound("No user with this id found"));

        if (userRepository.existsByEmailAndIdNot(up.email(), user.getId())) {
            throw new UserAlreadyExists("User with this email already exists, please enter a different email address");
        }
        user.setEmail(up.email());
        user.setFullName(up.fullName());
        user.setPhone(up.phone());
        user.setBillingAddress(up.billingAddress());
        user.setCountry(up.country());
        user.setShippingAddress(up.shippingAddress());

        userRepository.save(user);

        return UserMapper.userToResponseDto(user);
}

    private UserRole resolveRequestedRole(CreateUserRequest createUserRequest) {
        String requestedRole = createUserRequest.userRole();

        if (!StringUtils.hasText(requestedRole)) {
            return UserRole.CLIENT;
        }

        UserRole desiredRole;
        try {
            desiredRole = UserRole.valueOf(requestedRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user role: " + requestedRole);
        }

        if (!isCurrentUserAdmin() && desiredRole != UserRole.CLIENT) {
            throw new AccessDeniedException("Only administrators can create privileged accounts");
        }

        return desiredRole;
    }

    private boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null) {
            return false;
        }

        return authorities.stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
