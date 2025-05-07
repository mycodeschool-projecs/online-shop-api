package mycode.online_shop_api.app.users.mock;

import mycode.online_shop_api.app.system.security.UserRole;
import mycode.online_shop_api.app.users.model.User;

public class UserMockData {

    public static User createUser(String email, String name) {
        return User.builder()
                .email(email)
                .fullName(name)
                .password("testPassword123")
                .country("")
                .shippingAddress("")
                .billingAddress("")
                .phone("")
                .userRole(UserRole.CLIENT)
                .build();
    }
}