package mycode.online_shop_api.app.cart.dtos;



public record CartProductResponse( int productId,
         String name,
         String category,
         Double price,
         int quantity) {
}
