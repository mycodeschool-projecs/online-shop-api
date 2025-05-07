package mycode.online_shop_api.app.orderDetails.service;

import mycode.online_shop_api.app.orderDetails.dtos.CreateOrderDetailsRequest;

public interface OrderDetailsCommandService {

    void addOrderDetails(CreateOrderDetailsRequest createOrderDetailsRequest);
}
