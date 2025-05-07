package mycode.online_shop_api.app.categories.service;


import lombok.AllArgsConstructor;
import mycode.online_shop_api.app.categories.dtos.CategoryResponse;
import mycode.online_shop_api.app.categories.dtos.CategoryResponseList;
import mycode.online_shop_api.app.categories.exceptions.NoCategoryFound;
import mycode.online_shop_api.app.categories.mapper.CategoryMapper;
import mycode.online_shop_api.app.categories.model.Category;
import mycode.online_shop_api.app.categories.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService{

    private CategoryRepository categoryRepository;


    @Override
    public CategoryResponseList getAllCategories() {
        List<Category> list = categoryRepository.findAll();

        ArrayList<CategoryResponse> responses = new ArrayList<>();

        list.forEach(category -> {
            responses.add(CategoryMapper.categoryToResponseDto(category));
        });

        return new CategoryResponseList(responses);
    }

}
