package mycode.online_shop_api.app.categories.dtos;

import lombok.Builder;

@Builder
public record CategoryParentResponse(int id, String name) {
}
