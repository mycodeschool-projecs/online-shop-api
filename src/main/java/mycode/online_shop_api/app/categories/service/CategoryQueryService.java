package mycode.online_shop_api.app.categories.service;

import mycode.online_shop_api.app.categories.dtos.CategoryResponse;
import mycode.online_shop_api.app.categories.dtos.CategoryResponseList;

public interface CategoryQueryService {


    CategoryResponseList getAllCategories();
}
