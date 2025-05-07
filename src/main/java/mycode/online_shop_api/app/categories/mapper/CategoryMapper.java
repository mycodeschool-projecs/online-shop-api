package mycode.online_shop_api.app.categories.mapper;

import mycode.online_shop_api.app.categories.dtos.CategoryParentResponse;
import mycode.online_shop_api.app.categories.dtos.CategoryResponse;
import mycode.online_shop_api.app.categories.model.Category;

public class CategoryMapper {

    public static CategoryResponse categoryToResponseDto(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .parent(category.getParent() != null ?
                        CategoryParentResponse.builder()
                                .id(category.getParent().getId())
                                .name(category.getParent().getName())
                                .build()
                        : null)
                .build();
    }
}
