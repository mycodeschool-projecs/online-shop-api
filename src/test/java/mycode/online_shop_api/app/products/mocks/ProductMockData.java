package mycode.online_shop_api.app.products.mocks;

import mycode.online_shop_api.app.products.model.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductMockData {


    public static Product createProduct(String name, double price, int stock, double weight, String descriptions, String category, LocalDate createDate) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        product.setWeight(weight);
        product.setDescriptions(descriptions);
        product.setCategory(category);
        product.setCreateDate(createDate);
        return product;
    }

    public static List<Product> createSampleProducts() {
        List<Product> products = new ArrayList<>();
        products.add(createProduct("Cheap Phone",100, 10, 0.5, "Basic phone", "Phone", LocalDate.now()));
        products.add(createProduct("Mid Phone", 500, 5, 0.8, "Mid-range phone", "Phone", LocalDate.now()));
        products.add(createProduct("Expensive Phone",1500, 2, 1.0, "High-end phone", "Phone", LocalDate.now()));
        return products;
    }

    public static Product createCheapLaptop() {
        return createProduct("Cheap Laptop",400, 10, 2.0, "Budget laptop", "Laptop", LocalDate.now());
    }

    public static Product createGamingLaptop() {
        return createProduct("Gaming Laptop", 2000, 3, 3.0, "High performance gaming laptop", "Laptop", LocalDate.now());
    }

    public static Product createBusinessLaptop() {
        return createProduct("Business Laptop", 1000, 5, 2.5, "Business oriented laptop", "Laptop", LocalDate.now());
    }

}
