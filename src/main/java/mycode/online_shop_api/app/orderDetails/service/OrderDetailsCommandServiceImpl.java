package mycode.online_shop_api.app.orderDetails.service;

import mycode.online_shop_api.app.orderDetails.dtos.CreateOrderDetailsRequest;
import mycode.online_shop_api.app.orderDetails.model.OrderDetails;
import mycode.online_shop_api.app.orderDetails.repository.OrderDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsCommandServiceImpl implements OrderDetailsCommandService{

    private final OrderDetailsRepository orderDetailsRepository;

    public OrderDetailsCommandServiceImpl(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
    }


    public void addOrderDetails(CreateOrderDetailsRequest createOrderDetailsRequest){

        OrderDetails orderDetails = OrderDetails.builder().order(createOrderDetailsRequest.order()).price(createOrderDetailsRequest.price()).product(createOrderDetailsRequest.product()).quantity(createOrderDetailsRequest.quantity()).build();

        orderDetailsRepository.saveAndFlush(orderDetails);
    }


}
