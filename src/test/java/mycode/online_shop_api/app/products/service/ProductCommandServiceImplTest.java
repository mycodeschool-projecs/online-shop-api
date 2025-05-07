package mycode.online_shop_api.app.products.service;

import mycode.online_shop_api.app.categories.exceptions.NoCategoryFound;
import mycode.online_shop_api.app.categories.model.Category;
import mycode.online_shop_api.app.categories.repository.CategoryRepository;
import mycode.online_shop_api.app.productCategories.repository.ProductCategoriesRepository;
import mycode.online_shop_api.app.products.dto.CreateProductRequest;
import mycode.online_shop_api.app.products.dto.UpdateProductRequest;
import mycode.online_shop_api.app.products.exceptions.NoProductFound;
import mycode.online_shop_api.app.products.mocks.ProductMockData;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductCommandServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductCategoriesRepository productCategoriesRepository;

    @InjectMocks
    private ProductCommandServiceImpl productCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddProductSuccessfully() {
        CreateProductRequest request = new CreateProductRequest(
                "Laptop", "High performance gaming laptop", "Gaming Laptop",
                2000, 3, 3.0
        );

        Category mockCategory = new Category();
        mockCategory.setName("Laptop");


        when(categoryRepository.findByName("Laptop")).thenReturn(Optional.of(mockCategory));
        when(productRepository.saveAndFlush(any())).thenReturn(ProductMockData.createGamingLaptop());

        var response = productCommandService.addProduct(request);

        assertEquals("Gaming Laptop", response.name());
        assertEquals("Laptop", response.category());
        verify(productCategoriesRepository).saveAndFlush(any());
    }

    @Test
    void shouldThrowWhenCategoryNotFound() {
        CreateProductRequest request = new CreateProductRequest(
                "Phone", "Nonexistent", "description",
                10, 5, 0.3
        );

        when(categoryRepository.findByName("Nonexistent")).thenReturn(Optional.empty());

        assertThrows(NoCategoryFound.class, () -> productCommandService.addProduct(request));
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        Product product = ProductMockData.createCheapLaptop();
        product.setId(1);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        var response = productCommandService.deleteProduct(1);

        assertEquals("Cheap Laptop", response.name());
        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentProduct() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoProductFound.class, () -> productCommandService.deleteProduct(99));
    }

    @Test
    void shouldUpdateProduct() {
        Product product = ProductMockData.createCheapLaptop();
        product.setId(1);

        UpdateProductRequest updateRequest = new UpdateProductRequest(
                "UpdatedCat", "Updated Desc", "Updated Laptop",
                999, 20, 1.8
        );

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        productCommandService.updateProductPut(1, updateRequest);

        assertEquals("Updated Laptop", product.getName());
        assertEquals("UpdatedCat", product.getCategory());
        assertEquals("Updated Desc", product.getDescriptions());
        verify(productRepository).saveAndFlush(product);
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        UpdateProductRequest updateRequest = new UpdateProductRequest(
                "name", "cat", "desc", 1, 1, 1.0
        );

        assertThrows(NoProductFound.class, () -> productCommandService.updateProductPut(1, updateRequest));
    }
}