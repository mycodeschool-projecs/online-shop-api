package mycode.online_shop_api.app.products.service;

import mycode.online_shop_api.app.products.dto.CreateProductRequest;
import mycode.online_shop_api.app.products.dto.ProductResponse;
import mycode.online_shop_api.app.products.dto.UpdateProductRequest;

public interface ProductCommandService {

    ProductResponse addProduct(CreateProductRequest createProductRequest);

    ProductResponse deleteProduct(int id);

    void updateProductPut(int id, UpdateProductRequest updateProductRequest);

}
