package mycode.online_shop_api.app.users.dtos;


import mycode.online_shop_api.app.system.security.UserRole;

public record LoginResponse(String jwtToken,
                            Long id,
                            String fullName,
                            String phone,
                            String email,
                            UserRole userRole) {
}
