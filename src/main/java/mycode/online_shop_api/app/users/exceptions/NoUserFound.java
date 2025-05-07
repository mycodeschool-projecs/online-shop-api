package mycode.online_shop_api.app.users.exceptions;

public class NoUserFound extends RuntimeException {
    public NoUserFound(String message) {
        super(message);
    }
}
