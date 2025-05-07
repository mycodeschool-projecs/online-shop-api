package mycode.online_shop_api.app.orderDetails.service;

import lombok.AllArgsConstructor;
import mycode.online_shop_api.app.orderDetails.repository.OrderDetailsRepository;
import mycode.online_shop_api.app.products.dto.ProductResponse;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OrderDetailsQueryServiceImpl implements OrderDetailsQueryService {


    private OrderDetailsRepository orderDetailsRepository;
    private ProductRepository productRepository;

    @Override
    public ProductResponse mostSoldProduct() {
        List<Integer> list = orderDetailsRepository.mostSoldProduct();

        Optional<Product> product = productRepository.findById(list.get(0));

        return ProductResponse.builder()
                .category(product.get().getCategory())
                .createDate(product.get().getCreateDate())
                .id(product.get().getId())
                .description(product.get().getDescriptions())
                .name(product.get().getName())
                .price(product.get().getPrice())
                .stock(product.get().getStock())
                .weight(product.get().getWeight())
                .build();
    }
}
