package mycode.online_shop_api.app.orderDetails.service;

import mycode.online_shop_api.app.orderDetails.repository.OrderDetailsRepository;
import mycode.online_shop_api.app.products.dto.ProductResponse;
import mycode.online_shop_api.app.products.exceptions.NoProductFound;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.mocks.ProductMockData;
import mycode.online_shop_api.app.products.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderDetailsQueryServiceImplTest {

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderDetailsQueryServiceImpl orderDetailsQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindMostSoldProduct() {
        Product mostSoldProduct = ProductMockData.createProduct(
                "Gaming Laptop",
                2000.0,
                10,
                3.0,
                "High performance gaming laptop",
                "Laptops",
                null
        );
        mostSoldProduct.setId(1);

        when(orderDetailsRepository.mostSoldProduct()).thenReturn(List.of(1));
        when(productRepository.findById(1)).thenReturn(Optional.of(mostSoldProduct));

        ProductResponse response = orderDetailsQueryService.mostSoldProduct();

        assertNotNull(response);
        assertEquals("Gaming Laptop", response.name());
        assertEquals("Laptops", response.category());
        assertEquals(2000.0, response.price());
        assertEquals(1, response.id());
    }

    @Test
    void shouldThrowExceptionWhenNoProductsSold() {
        when(orderDetailsRepository.mostSoldProduct()).thenReturn(List.of());

        assertThrows(NoProductFound.class, () -> orderDetailsQueryService.mostSoldProduct());
    }

    @Test
    void shouldThrowExceptionWhenMostSoldProductNotFound() {

        when(orderDetailsRepository.mostSoldProduct()).thenReturn(List.of(999));
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NoProductFound.class, () -> orderDetailsQueryService.mostSoldProduct());
    }
}
