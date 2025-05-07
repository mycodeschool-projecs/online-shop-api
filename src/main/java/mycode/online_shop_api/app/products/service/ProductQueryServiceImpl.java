package mycode.online_shop_api.app.products.service;

import lombok.AllArgsConstructor;
import mycode.online_shop_api.app.orderDetails.repository.OrderDetailsRepository;
import mycode.online_shop_api.app.products.dto.ProductResponse;
import mycode.online_shop_api.app.products.dto.ProductResponseList;
import mycode.online_shop_api.app.products.exceptions.NoProductFound;
import mycode.online_shop_api.app.products.mapper.ProductMapper;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class ProductQueryServiceImpl implements ProductQueryService{

    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;



    @Override
    public ProductResponse findById(int id) {
        Optional<Product> product = productRepository.findById(id);

        if(product.isPresent()){
            return new ProductResponse(product.get().getId(),product.get().getCategory(),product.get().getCreateDate(),product.get().getDescriptions(),product.get().getName(),product.get().getPrice(),product.get().getStock(),product.get().getWeight());

        }else{
            throw new NoProductFound(" ");
        }
    }

    @Override
    public ProductResponseList getAllProducts() {
        List<Product> list = productRepository.findAll();

        List<ProductResponse> responses = new ArrayList<>();

        list.forEach(product -> {
            responses.add(ProductMapper.productToResponseDto(product));
        });
        return new ProductResponseList(responses);
    }

    @Override
    public ProductResponseList getTopSellingProducts() {
        List<Product> topSellingProducts = orderDetailsRepository.findTopSellingProducts();

        List<ProductResponse> responseList = new ArrayList<>();

        topSellingProducts.forEach(product -> {
            responseList.add(ProductMapper.productToResponseDto(product));
        });

        return new ProductResponseList(responseList);
    }

    @Override
    public int totalProducts(){
        return productRepository.findAll().size();
    }
}
