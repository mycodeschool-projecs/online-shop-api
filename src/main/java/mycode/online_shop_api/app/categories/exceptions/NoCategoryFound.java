package mycode.online_shop_api.app.categories.exceptions;

public class NoCategoryFound extends RuntimeException {
    public NoCategoryFound(String message) {
        super(message);
    }
}
