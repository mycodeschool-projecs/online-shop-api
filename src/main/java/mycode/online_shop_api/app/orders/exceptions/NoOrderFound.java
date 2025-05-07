package mycode.online_shop_api.app.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoOrderFound extends RuntimeException {
    public NoOrderFound(String message) {
        super(message);
    }
}
