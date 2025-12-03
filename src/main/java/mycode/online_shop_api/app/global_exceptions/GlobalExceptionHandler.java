package mycode.online_shop_api.app.global_exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import mycode.online_shop_api.app.cart.exceptions.NoCartFound;
import mycode.online_shop_api.app.categories.exceptions.CategoryAlreadyExists;
import mycode.online_shop_api.app.categories.exceptions.NoCategoryFound;
import mycode.online_shop_api.app.products.exceptions.NoProductFound;
import mycode.online_shop_api.app.users.exceptions.NoUserFound;
import mycode.online_shop_api.app.users.exceptions.UserAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoUserFound.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(NoUserFound exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ApiError> handleUserExistsException(UserAlreadyExists exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(NoCartFound.class)
    public ResponseEntity<ApiError> handleCartNotFoundException(NoCartFound exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(NoProductFound.class)
    public ResponseEntity<ApiError> handleProductNotFoundException(NoProductFound exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(NoCategoryFound.class)
    public ResponseEntity<ApiError> handleCategoryNotFound(NoCategoryFound exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(CategoryAlreadyExists.class)
    public ResponseEntity<ApiError> handleCategoryExists(CategoryAlreadyExists exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException exception,
                                                           HttpServletRequest request) {
        List<String> details = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", request, details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolations(ConstraintViolationException exception,
                                                               HttpServletRequest request) {
        List<String> details = exception.getConstraintViolations().stream()
                .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                .collect(Collectors.toList());
        return buildResponse(HttpStatus.BAD_REQUEST, "Constraint violation", request, details);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException exception,
                                                       HttpServletRequest request) {
        String message = String.format("Parameter '%s' should be of type %s",
                exception.getName(), exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "unknown");
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException exception,
                                                         HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException exception,
                                                       HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, exception.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException exception,
                                                          HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception exception, HttpServletRequest request) {
        String message = exception.getMessage() == null ? "Unexpected error occurred" : exception.getMessage();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        return buildResponse(status, message, request, List.of());
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, HttpServletRequest request,
                                                   List<String> details) {
        ApiError apiError = ApiError.of(status, message, request.getRequestURI(), details);
        return ResponseEntity.status(status).body(apiError);
    }
}
