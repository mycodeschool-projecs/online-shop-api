package mycode.online_shop_api.app.users.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateUserRequest(@NotNull String fullName,
                                @NotNull String email,
                                @NotNull String password,
                                @NotNull String phone,
                                @NotNull String country,
                                @NotNull String billingAddress,
                                @NotNull String shippingAddress,
                                String userRole) {
}
