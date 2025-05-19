package mycode.online_shop_api.app.users.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mycode.online_shop_api.app.system.jwt.JWTTokenProvider;
import mycode.online_shop_api.app.users.dtos.*;
import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.service.UserCommandService;
import mycode.online_shop_api.app.users.service.UserQueryService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static mycode.online_shop_api.app.system.constants.Constants.JWT_TOKEN_HEADER;

/**
 * REST controller for user accounts and authentication.
 *
 * Security convention:
 *   – Roles stored in DB: ADMIN, CLIENT
 *   – GrantedAuthority values: ROLE_ADMIN, ROLE_CLIENT
 *   – Therefore annotations use hasRole / hasAnyRole without the ROLE_ prefix.
 *
 * Base path: /api/v1/users
 */
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /* ------------------------------------------------------------------ */
    /* Queries (admin-only unless stated otherwise)                        */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total")
    public ResponseEntity<Integer> totalUsers() {
        return ResponseEntity.ok(userQueryService.totalUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/most-active")
    public ResponseEntity<UserResponseList> mostActiveUsers() {
        return ResponseEntity.ok(userQueryService.getMostActiveUsers());
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @GetMapping("find/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userQueryService.findUserById(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<UserResponseList> getAllUsers() {
        return ResponseEntity.ok(userQueryService.getAllUsers());
    }

    /* ------------------------------------------------------------------ */
    /* Commands (CRUD)                                                    */
    /* ------------------------------------------------------------------ */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userCommandService.createUser(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{userId}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable long userId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userCommandService.deleteUser(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @PutMapping("edit/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable long userId,
                                                   @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userCommandService.updateUser(request, userId));
    }

    /* ------------------------------------------------------------------ */
    /* Authentication / Identity                                          */
    /* ------------------------------------------------------------------ */

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest credentials) {
        authenticate(credentials.email(), credentials.password());

        User loginUser = userQueryService.findByEmail(credentials.email());
        HttpHeaders jwtHeader = getJwtHeader(loginUser);

        LoginResponse response = new LoginResponse(
                jwtHeader.getFirst(JWT_TOKEN_HEADER),
                loginUser.getId(),
                loginUser.getFullName(),
                loginUser.getPhone(),
                loginUser.getEmail(),
                loginUser.getUserRole()
        );
        return new ResponseEntity<>(response, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody CreateUserRequest request) {
        userCommandService.createUser(request);
        User newUser = userQueryService.findByEmail(request.email());
        HttpHeaders jwtHeader = getJwtHeader(newUser);

        RegisterResponse response = new RegisterResponse(
                jwtHeader.getFirst(JWT_TOKEN_HEADER),
                newUser.getFullName(),
                newUser.getPhone(),
                newUser.getEmail(),
                newUser.getUserRole()
        );
        return new ResponseEntity<>(response, jwtHeader, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @GetMapping("/role")
    public ResponseEntity<String> getUserRole(@RequestHeader("Authorization") String token) {
        try {
            String tokenValue = extractToken(token);
            String username = jwtTokenProvider.getSubject(tokenValue);
            if (jwtTokenProvider.isTokenValid(username, tokenValue)) {
                User loginUser = userQueryService.findByEmail(username);
                return ResponseEntity.ok(loginUser.getUserRole().toString());
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while verifying token");
        }
    }

    /* ------------------------------------------------------------------ */
    /* Helper methods                                                     */
    /* ------------------------------------------------------------------ */

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJWTToken(user));
        return headers;
    }
}
