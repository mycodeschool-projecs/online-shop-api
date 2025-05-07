package mycode.online_shop_api.app.categories.service;


import mycode.online_shop_api.app.categories.dtos.CategoryResponse;
import mycode.online_shop_api.app.categories.dtos.CreateCategoryRequest;
import mycode.online_shop_api.app.categories.dtos.UpdateCategoryRequest;
import mycode.online_shop_api.app.categories.exceptions.CategoryAlreadyExists;
import mycode.online_shop_api.app.categories.exceptions.NoCategoryFound;
import mycode.online_shop_api.app.categories.mock.CategoryMockData;
import mycode.online_shop_api.app.categories.model.Category;
import mycode.online_shop_api.app.categories.repository.CategoryRepository;
import mycode.online_shop_api.app.categories.service.CategoryCommandServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryCommandServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryCommandServiceImpl categoryCommandService;

    private Category mockCategory;

    @BeforeEach
    void setUp() {
        mockCategory = CategoryMockData.createCategory("Electronics");
    }

    @Test
    void shouldAddCategorySuccessfully() {
        CreateCategoryRequest request = new CreateCategoryRequest("Electronics");

        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.empty());
        when(categoryRepository.saveAndFlush(any())).thenAnswer(invocation -> {
            Category savedCategory = invocation.getArgument(0);
            savedCategory.setId(1);
            return savedCategory;
        });

        CategoryResponse response = categoryCommandService.addCategory(request);

        assertNotNull(response);
        assertEquals("Electronics", response.name());
        verify(categoryRepository).saveAndFlush(any());
    }

    @Test
    void shouldThrowWhenCategoryAlreadyExists() {
        CreateCategoryRequest request = new CreateCategoryRequest("Electronics");

        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(mockCategory));

        assertThrows(CategoryAlreadyExists.class, () -> categoryCommandService.addCategory(request));
    }

    @Test
    void shouldDeleteCategorySuccessfully() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(mockCategory));

        CategoryResponse response = categoryCommandService.deleteCategory(1);

        assertEquals(mockCategory.getId(), response.id());
        assertEquals(mockCategory.getName(), response.name());
        verify(categoryRepository).delete(mockCategory);
    }

    @Test
    void shouldThrowWhenCategoryNotFoundForDeletion() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoCategoryFound.class, () -> categoryCommandService.deleteCategory(1));
    }

    @Test
    void shouldUpdateCategorySuccessfully() {
        UpdateCategoryRequest request = new UpdateCategoryRequest("New Name");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(mockCategory));

        CategoryResponse response = categoryCommandService.updateCategory(1, request);

        assertEquals("New Name", response.name());
        assertEquals(1, response.id());
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentCategory() {
        UpdateCategoryRequest request = new UpdateCategoryRequest("New Name");

        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoCategoryFound.class, () -> categoryCommandService.updateCategory(1, request));
    }

    @Test
    void shouldAddSubcategorySuccessfully() {
        CreateCategoryRequest request = new CreateCategoryRequest("Smartphones");
        Category parentCategory = new Category();
        parentCategory.setId(1);
        parentCategory.setName("Electronics");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findByName("Smartphones")).thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenAnswer(invocation -> {
            Category savedCategory = invocation.getArgument(0);
            savedCategory.setId(2);
            return savedCategory;
        });

        CategoryResponse response = categoryCommandService.addSubcategory(1, request);

        assertNotNull(response);
        assertEquals("Smartphones", response.name());
        verify(categoryRepository).save(any());
    }

    @Test
    void shouldThrowWhenParentCategoryNotFound() {
        CreateCategoryRequest request = new CreateCategoryRequest("Smartphones");

        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryCommandService.addSubcategory(1, request));
    }

    @Test
    void shouldThrowWhenSubcategoryAlreadyExists() {
        CreateCategoryRequest request = new CreateCategoryRequest("Smartphones");
        Category parentCategory = new Category();
        parentCategory.setId(1);
        parentCategory.setName("Electronics");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findByName("Smartphones")).thenReturn(Optional.of(mockCategory));

        assertThrows(CategoryAlreadyExists.class, () -> categoryCommandService.addSubcategory(1, request));
    }
}
