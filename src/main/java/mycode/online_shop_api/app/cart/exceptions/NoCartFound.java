package mycode.online_shop_api.app.cart.exceptions;

public class NoCartFound extends RuntimeException {
    public NoCartFound(String message) {
        super(message);
    }
}
