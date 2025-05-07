package mycode.online_shop_api.app.users.dtos;

import mycode.online_shop_api.app.system.security.UserRole;

public record UserResponse(long id, String email, String password, String fullName, String phone, UserRole userRole, String country, String billing, String shipping) {
}
