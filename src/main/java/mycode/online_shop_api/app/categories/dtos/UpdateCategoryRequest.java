package mycode.online_shop_api.app.categories.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateCategoryRequest(@NotNull String name) {

}