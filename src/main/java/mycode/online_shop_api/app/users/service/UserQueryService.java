    package mycode.online_shop_api.app.users.service;

    import mycode.online_shop_api.app.users.dtos.UserResponse;
    import mycode.online_shop_api.app.users.dtos.UserResponseList;
    import mycode.online_shop_api.app.users.model.User;

    public interface UserQueryService {

        UserResponse findUserById(long id);

        UserResponseList getAllUsers();

        User findByEmail(String email);

        UserResponseList getMostActiveUsers();

        int totalUsers();

    }
