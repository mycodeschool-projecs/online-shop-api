package mycode.online_shop_api.app.users.service;

import lombok.AllArgsConstructor;
import mycode.online_shop_api.app.orders.repository.OrderRepository;
import mycode.online_shop_api.app.users.dtos.UserResponse;
import mycode.online_shop_api.app.users.dtos.UserResponseList;
import mycode.online_shop_api.app.users.exceptions.NoUserFound;
import mycode.online_shop_api.app.users.mapper.UserMapper;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserQueryServiceImpl implements UserQueryService{

    private UserRepository userRepository;
    private OrderRepository orderRepository;

    @Override
    public UserResponse findUserById(long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoUserFound("No user with this id found"));

        return UserMapper.userToResponseDto(user);
    }

    @Override
    public UserResponseList getAllUsers() {
        List<User> list = userRepository.findAll();
        List<UserResponse> responses = new ArrayList<>();

        list.forEach(user -> {
            responses.add(UserMapper.userToResponseDto(user));
        });

        return new UserResponseList(responses);
    }

    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new NoUserFound("No user with this email found"));
    }

    @Override
    public UserResponseList getMostActiveUsers() {
        Optional<List<User>> mostActiveUsers = orderRepository.findMostActiveUsers();

        List<UserResponse> responseList = new ArrayList<>();

        mostActiveUsers.ifPresent(users -> users.forEach(user -> {
            responseList.add(UserMapper.userToResponseDto(user));
        }));

        return new UserResponseList(responseList);
    }

    @Override
    public int totalUsers() {
        return userRepository.findAll().size();
    }
}
