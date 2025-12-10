package mycode.online_shop_api.app.orderDetails.service;

import lombok.AllArgsConstructor;
import mycode.online_shop_api.app.orderDetails.repository.OrderDetailsRepository;
import mycode.online_shop_api.app.products.dto.ProductResponse;
import mycode.online_shop_api.app.products.exceptions.NoProductFound;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderDetailsQueryServiceImpl implements OrderDetailsQueryService {


    private OrderDetailsRepository orderDetailsRepository;
    private ProductRepository productRepository;

    @Override
    public ProductResponse mostSoldProduct() {
        List<Integer> list = orderDetailsRepository.mostSoldProduct();

        if (list.isEmpty()) {
            throw new NoProductFound("No products have been sold yet");
        }

        Integer productId = list.get(0);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoProductFound("Most sold product no longer exists"));

        return ProductResponse.builder()
                .category(product.getCategory())
                .createDate(product.getCreateDate())
                .id(product.getId())
                .description(product.getDescriptions())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .weight(product.getWeight())
                .build();
    }
}
