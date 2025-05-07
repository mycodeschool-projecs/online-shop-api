package mycode.online_shop_api.app.products.service;

import mycode.online_shop_api.app.orderDetails.repository.OrderDetailsRepository;
import mycode.online_shop_api.app.products.dto.ProductResponse;
import mycode.online_shop_api.app.products.dto.ProductResponseList;
import mycode.online_shop_api.app.products.exceptions.NoProductFound;
import mycode.online_shop_api.app.products.mocks.ProductMockData;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductQueryServiceImplTest {

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductQueryServiceImpl productQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindProductByIdSuccessfully() {
        Product product = ProductMockData.createGamingLaptop();
        product.setId(1);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductResponse response = productQueryService.findById(1);

        assertEquals("Gaming Laptop", response.name());
        assertEquals(2000, response.price());
        verify(productRepository).findById(1);
    }

    @Test
    void shouldThrowWhenProductNotFoundById() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoProductFound.class, () -> productQueryService.findById(99));
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> mockProducts = ProductMockData.createSampleProducts();

        when(productRepository.findAll()).thenReturn(mockProducts);

        ProductResponseList response = productQueryService.getAllProducts();

        assertEquals(3, response.list().size());
        verify(productRepository).findAll();
    }

    @Test
    void shouldGetEmptyListWhenNoProductsExist() {
        when(productRepository.findAll()).thenReturn(List.of());

        ProductResponseList response = productQueryService.getAllProducts();

        assertTrue(response.list().isEmpty());
        verify(productRepository).findAll();
    }

    @Test
    void shouldGetTopSellingProducts() {
        List<Product> mockTopSellers = List.of(
                ProductMockData.createGamingLaptop(),
                ProductMockData.createCheapLaptop()
        );

        when(orderDetailsRepository.findTopSellingProducts()).thenReturn(mockTopSellers);

        ProductResponseList response = productQueryService.getTopSellingProducts();

        assertEquals(2, response.list().size());
        verify(orderDetailsRepository).findTopSellingProducts();
    }

    @Test
    void shouldReturnZeroWhenNoTopSellingProducts() {
        when(orderDetailsRepository.findTopSellingProducts()).thenReturn(List.of());

        ProductResponseList response = productQueryService.getTopSellingProducts();

        assertTrue(response.list().isEmpty());
        verify(orderDetailsRepository).findTopSellingProducts();
    }

    @Test
    void shouldReturnTotalProductsCount() {
        List<Product> mockProducts = ProductMockData.createSampleProducts();

        when(productRepository.findAll()).thenReturn(mockProducts);

        int count = productQueryService.totalProducts();

        assertEquals(3, count);
        verify(productRepository).findAll();
    }

    @Test
    void shouldReturnZeroWhenNoProductsExist() {
        when(productRepository.findAll()).thenReturn(List.of());

        int count = productQueryService.totalProducts();

        assertEquals(0, count);
        verify(productRepository).findAll();
    }
}