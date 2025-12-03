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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserCommandServiceImpl implements UserCommandService{

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private CartRepository cartRepository;


    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {

        String roleString = createUserRequest.userRole().toUpperCase();
        UserRole role;

        try {
            role = UserRole.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user role: " + roleString);
        }

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
}
