package mycode.online_shop_api.app.global_exceptions;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Standardized shape for error responses returned by {@link GlobalExceptionHandler}.
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) {

    public static ApiError of(HttpStatus httpStatus, String message, String path, List<String> details) {
        List<String> safeDetails = details == null ? Collections.emptyList() : List.copyOf(details);
        return new ApiError(
                Instant.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message,
                path,
                safeDetails
        );
    }
}
