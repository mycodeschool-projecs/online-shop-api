package mycode.online_shop_api.app.global_exceptions;

import jakarta.servlet.http.HttpServletRequest;
import mycode.online_shop_api.app.users.exceptions.NoUserFound;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleUserNotFoundReturnsApiError() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/users/find/42");
        ResponseEntity<ApiError> response = handler.handleUserNotFoundException(new NoUserFound("User missing"), request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ApiError body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.status());
        assertEquals("/api/v1/users/find/42", body.path());
        assertEquals("User missing", body.message());
    }

    @Test
    void handleIllegalArgumentReturnsBadRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/products/add");
        ResponseEntity<ApiError> response = handler.handleIllegalArgument(new IllegalArgumentException("Invalid payload"), request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid payload", response.getBody().message());
    }
}
