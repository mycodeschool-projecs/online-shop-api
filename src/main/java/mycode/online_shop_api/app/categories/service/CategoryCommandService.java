package mycode.online_shop_api.app.categories.service;

import mycode.online_shop_api.app.categories.dtos.CategoryResponse;
import mycode.online_shop_api.app.categories.dtos.CreateCategoryRequest;
import mycode.online_shop_api.app.categories.dtos.UpdateCategoryRequest;

public interface CategoryCommandService {

    CategoryResponse addCategory(CreateCategoryRequest createCategoryRequest);

    CategoryResponse deleteCategory(int id);

    CategoryResponse updateCategory(int id, UpdateCategoryRequest updateCategoryRequest);

    CategoryResponse addSubcategory(int parentId, CreateCategoryRequest createCategoryRequest);

}
