package mycode.online_shop_api.app.products.dto;

public record UpdateProductRequest (
        String category,
        String description,
        String name,
        double price,
        int stock,
        double weight){
}
