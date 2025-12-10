package mycode.online_shop_api.app.categories.service;

import lombok.AllArgsConstructor;
import mycode.online_shop_api.app.categories.dtos.CategoryResponse;
import mycode.online_shop_api.app.categories.dtos.CreateCategoryRequest;
import mycode.online_shop_api.app.categories.dtos.UpdateCategoryRequest;
import mycode.online_shop_api.app.categories.exceptions.CategoryAlreadyExists;
import mycode.online_shop_api.app.categories.exceptions.NoCategoryFound;
import mycode.online_shop_api.app.categories.mapper.CategoryMapper;
import mycode.online_shop_api.app.categories.model.Category;
import mycode.online_shop_api.app.categories.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryCommandServiceImpl implements CategoryCommandService{

    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponse addCategory(CreateCategoryRequest createCategoryRequest) {
        Optional<Category> categoryExists = categoryRepository.findByName(createCategoryRequest.name());

        if(categoryExists.isPresent()){
            throw new CategoryAlreadyExists("Category with this name already exists");
        }

        Category category = Category.builder()
                .name(createCategoryRequest.name()).build();

        categoryRepository.saveAndFlush(category);

        return CategoryMapper.categoryToResponseDto(category);
    }

    @Override
    public CategoryResponse deleteCategory(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoCategoryFound("No category with this id found"));

        CategoryResponse categoryResponse = CategoryResponse.builder().id(category.getId()).name(category.getName()).build();

        categoryRepository.delete(category);

        return categoryResponse;
    }

    @Override
    public CategoryResponse updateCategory(int id, UpdateCategoryRequest updateCategoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoCategoryFound("No category with this id found"));

        category.setName(updateCategoryRequest.name());

        categoryRepository.save(category);

        return CategoryResponse.builder().name(category.getName()).id(category.getId()).build();
    }

    @Override
    @Transactional
    public CategoryResponse addSubcategory(int parentId, CreateCategoryRequest createCategoryRequest) {
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent category not found"));

        Optional<Category> category = categoryRepository.findByName(createCategoryRequest.name());

        if(category.isPresent()){
            throw new CategoryAlreadyExists("Category with this name already exists");
        }

        Category subcategory = new Category();
        subcategory.setName(createCategoryRequest.name());
        subcategory.setParent(parentCategory);
        categoryRepository.save(subcategory);

        return CategoryMapper.categoryToResponseDto(subcategory);
    }



}
