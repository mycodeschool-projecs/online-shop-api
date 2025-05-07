package mycode.online_shop_api.app.global_exceptions;


import mycode.online_shop_api.app.cart.exceptions.NoCartFound;
import mycode.online_shop_api.app.categories.exceptions.CategoryAlreadyExists;
import mycode.online_shop_api.app.categories.exceptions.NoCategoryFound;
import mycode.online_shop_api.app.products.exceptions.NoProductFound;
import mycode.online_shop_api.app.users.exceptions.NoUserFound;
import mycode.online_shop_api.app.users.exceptions.UserAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoUserFound.class})
    public ResponseEntity<Object> handleUserNotFoundException(NoUserFound exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }


    @ExceptionHandler({UserAlreadyExists.class})
    public ResponseEntity<Object> handleUserExistsException(UserAlreadyExists exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler({NoCartFound.class})
    public ResponseEntity<Object> handleCartNotFoundException(NoCartFound exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({NoProductFound.class})
    public ResponseEntity<Object> handleProductNotFoundException(NoProductFound exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({NoCategoryFound.class})
    public ResponseEntity<Object> handleCategoryNotFound(NoCategoryFound exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }


    @ExceptionHandler({CategoryAlreadyExists.class})
    public ResponseEntity<Object> handleCategoryExists(CategoryAlreadyExists exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }



}
