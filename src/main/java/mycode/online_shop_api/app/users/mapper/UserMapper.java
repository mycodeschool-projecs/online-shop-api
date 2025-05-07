package mycode.online_shop_api.app.users.mapper;


import mycode.online_shop_api.app.users.dtos.CreateUserRequest;
import mycode.online_shop_api.app.users.dtos.UserResponse;
import mycode.online_shop_api.app.users.model.User;

public class UserMapper {

    public static UserResponse userToResponseDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getFullName(),
                user.getPhone(),
                user.getUserRole(),
                user.getCountry(),
                user.getBillingAddress(),
                user.getShippingAddress());
    }

    public static User userRequestDtoToUser(CreateUserRequest createUserRequest) {
        return User.builder()
                .email(createUserRequest.email())
                .fullName(createUserRequest.fullName())
                .password(createUserRequest.password())
                .phone(createUserRequest.phone()).build();
    }

}
