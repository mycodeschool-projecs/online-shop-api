package mycode.online_shop_api.app.products.service;

import mycode.online_shop_api.app.products.dto.ProductResponse;
import mycode.online_shop_api.app.products.dto.ProductResponseList;

public interface ProductQueryService {

    ProductResponse findById(int id);

    ProductResponseList getAllProducts();

    ProductResponseList getTopSellingProducts();

    int totalProducts();
}
