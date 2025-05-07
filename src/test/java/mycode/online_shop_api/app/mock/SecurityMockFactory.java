package mycode.online_shop_api.app.mock;

import jakarta.servlet.http.HttpServletRequest;
import mycode.online_shop_api.app.system.jwt.JWTTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class SecurityMockFactory {

    private static final String SECRET_KEY = "asdasdasdasdasd123344asdasdasdasdasasd12321332sadas";

    public static String ADMIN_TOKEN;
    public static String CLIENT_TOKEN;
    public static String INVALID_TOKEN = "invalid-token";

    public static final String ADMIN_USERNAME = "adminUser";
    public static final String CLIENT_USERNAME = "clientUser";


    public static void setupTokens(JWTTokenProvider jwtTokenProvider) {

        UserDetails adminUser = new User(ADMIN_USERNAME, "password",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        UserDetails clientUser = new User(CLIENT_USERNAME, "password",
                List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));


        ADMIN_TOKEN = jwtTokenProvider.generateJWTToken(adminUser);
        CLIENT_TOKEN = jwtTokenProvider.generateJWTToken(clientUser);
    }

    public static void mockAdminToken(JWTTokenProvider jwtTokenProvider) {
        when(jwtTokenProvider.getSubject(ADMIN_TOKEN)).thenReturn(ADMIN_USERNAME);
        when(jwtTokenProvider.isTokenValid(ADMIN_USERNAME, ADMIN_TOKEN)).thenReturn(true);
        when(jwtTokenProvider.getAuthorities(ADMIN_TOKEN))
                .thenReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtTokenProvider.getAuthentication(
                eq(ADMIN_USERNAME),
                eq(List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))),
                any(HttpServletRequest.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        ADMIN_USERNAME, null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    }

    public static void mockClientToken(JWTTokenProvider jwtTokenProvider) {
        when(jwtTokenProvider.getSubject(CLIENT_TOKEN)).thenReturn(CLIENT_USERNAME);
        when(jwtTokenProvider.isTokenValid(CLIENT_USERNAME, CLIENT_TOKEN)).thenReturn(true);
        when(jwtTokenProvider.getAuthorities(CLIENT_TOKEN))
                .thenReturn(List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));

        when(jwtTokenProvider.getAuthentication(
                eq(CLIENT_USERNAME),
                eq(List.of(new SimpleGrantedAuthority("ROLE_CLIENT"))),
                any(HttpServletRequest.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        CLIENT_USERNAME, null, List.of(new SimpleGrantedAuthority("ROLE_CLIENT"))));
    }

    public static void mockInvalidToken(JWTTokenProvider jwtTokenProvider) {
        when(jwtTokenProvider.getSubject(INVALID_TOKEN)).thenReturn(null);
        when(jwtTokenProvider.isTokenValid(null, INVALID_TOKEN)).thenReturn(false);
    }
}