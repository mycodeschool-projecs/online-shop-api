package mycode.online_shop_api.app.orderDetails.service;

import mycode.online_shop_api.app.orderDetails.dtos.CreateOrderDetailsRequest;
import mycode.online_shop_api.app.orderDetails.model.OrderDetails;
import mycode.online_shop_api.app.orderDetails.repository.OrderDetailsRepository;
import mycode.online_shop_api.app.orders.model.Order;
import mycode.online_shop_api.app.orders.mock.OrderMockData;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.mocks.ProductMockData;
import mycode.online_shop_api.app.orderDetails.mock.OrderDetailsMockData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderDetailsCommandServiceImplTest {

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @InjectMocks
    private OrderDetailsCommandServiceImpl orderDetailsCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddOrderDetailsSuccessfully() {

        Product testProduct = ProductMockData.createProduct(
                "Test Laptop",
                2000.0,
                10,
                3.0,
                "High performance laptop",
                "Laptops",
                null
        );

        Order testOrder = OrderMockData.createOrder(null);

        CreateOrderDetailsRequest request = new CreateOrderDetailsRequest(
                2000.0,
                2,
                testOrder,
                testProduct
        );

        when(orderDetailsRepository.saveAndFlush(any(OrderDetails.class))).thenReturn(
                OrderDetailsMockData.createOrderDetails(testOrder, testProduct, 2000.0, 2)
        );


        orderDetailsCommandService.addOrderDetails(request);


        verify(orderDetailsRepository).saveAndFlush(argThat(orderDetails ->
                orderDetails.getOrder().equals(testOrder) &&
                        orderDetails.getProduct().equals(testProduct) &&
                        orderDetails.getPrice() == 2000.0 &&
                        orderDetails.getQuantity() == 2
        ));
    }

    @Test
    void shouldSaveOrderDetailsWithAllFields() {
        Product testProduct = ProductMockData.createProduct(
                "Gaming Mouse",
                50.0,
                20,
                0.5,
                "High precision gaming mouse",
                "Accessories",
                null
        );

        Order testOrder = OrderMockData.createOrder(null);

        CreateOrderDetailsRequest request = new CreateOrderDetailsRequest(
                50.0,
                3,
                testOrder,
                testProduct
        );


        orderDetailsCommandService.addOrderDetails(request);


        verify(orderDetailsRepository).saveAndFlush(argThat(orderDetails -> {
            assertNotNull(orderDetails.getOrder());
            assertNotNull(orderDetails.getProduct());
            assertEquals(50.0, orderDetails.getPrice());
            assertEquals(3, orderDetails.getQuantity());
            return true;
        }));
    }
}