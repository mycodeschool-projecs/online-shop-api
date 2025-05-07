package mycode.online_shop_api.app.products.mapper;

import lombok.Builder;
import mycode.online_shop_api.app.products.dto.ProductResponse;
import mycode.online_shop_api.app.products.model.Product;

@Builder
public class ProductMapper {


    public static ProductResponse productToResponseDto(Product product){
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

    public static Product responseDtoToProduct(ProductResponse product){
        return Product.builder()
                .category(product.category())
                .createDate(product.createDate())
                .id(product.id())
                .descriptions(product.description())
                .name(product.name())
                .price(product.price())
                .stock(product.stock())
                .weight(product.weight())
                .build();
    }

}
