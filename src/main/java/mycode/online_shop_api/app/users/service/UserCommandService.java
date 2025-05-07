package mycode.online_shop_api.app.users.service;

import mycode.online_shop_api.app.users.dtos.CreateUserRequest;
import mycode.online_shop_api.app.users.dtos.UpdateUserRequest;
import mycode.online_shop_api.app.users.dtos.UserResponse;

public interface UserCommandService {

    UserResponse createUser(CreateUserRequest createUserRequest);

    UserResponse deleteUser(long id);

    UserResponse updateUser(UpdateUserRequest updateUserRequest, long id);

}
