package mycode.online_shop_api.app.categories.service;

import mycode.online_shop_api.app.categories.dtos.CategoryResponse;
import mycode.online_shop_api.app.categories.dtos.CategoryResponseList;
import mycode.online_shop_api.app.categories.mapper.CategoryMapper;
import mycode.online_shop_api.app.categories.mock.CategoryMockData;
import mycode.online_shop_api.app.categories.model.Category;
import mycode.online_shop_api.app.categories.repository.CategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryQueryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryQueryServiceImpl categoryQueryService;

    private List<Category> mockCategories;

    @BeforeEach
    void setUp() {
        mockCategories = Arrays.asList(
                CategoryMockData.createCategory("Electronics"),
                CategoryMockData.createCategory("Clothing")
        );
    }

    @Test
    void shouldGetAllCategoriesSuccessfully() {
        when(categoryRepository.findAll()).thenReturn(mockCategories);

        CategoryResponseList responseList = categoryQueryService.getAllCategories();

        assertNotNull(responseList);
        assertEquals(2, responseList.list().size());

        assertTrue(responseList.list().stream()
                .anyMatch(category -> category.name().equals("Electronics")));
        assertTrue(responseList.list().stream()
                .anyMatch(category -> category.name().equals("Clothing")));

        verify(categoryRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoriesExist() {

        when(categoryRepository.findAll()).thenReturn(List.of());

        CategoryResponseList responseList = categoryQueryService.getAllCategories();

        assertNotNull(responseList);
        assertTrue(responseList.list().isEmpty());

        verify(categoryRepository).findAll();
    }
}