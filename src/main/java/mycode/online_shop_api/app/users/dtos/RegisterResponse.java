package mycode.online_shop_api.app.users.dtos;


import mycode.online_shop_api.app.system.security.UserRole;

public record RegisterResponse(String jwtToken,
                               String fullName,
                               String phone,
                               String email,
                               UserRole userRole) {
}
