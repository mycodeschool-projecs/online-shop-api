package mycode.online_shop_api.app.categories.dtos;

import lombok.Builder;


@Builder
public record CategoryResponse(int id, String name, CategoryParentResponse parent) {
}
