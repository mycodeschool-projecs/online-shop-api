package mycode.online_shop_api.app.system.constants;

public class Constants {

    public static final String MY_CODE="MY CODE";

    public static final String ADMINISTRATION = "ADMINISTRATION";

    public static final String AUTHORITIES = "authorities";

    public static final String TOKEN_CANNOT_BE_VERIFIED = "token cannot be verified";

    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";

    public static final String TOKEN_PREFIX = "Bearer";

    public static final String FORBIDDEN_MESSAGE = "MESSAGE";

    public static final String ACCESS_DENIED_MESSAGE = "MESSAGE";

    public static final String APPLICATION_JSON_VALUE = "APPLICATION VALUE";

    public static final String[] PUBLIC_URLS = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/**",
            "/api/v1/users/login",
            "/api/v1/users/register"
    };

    public static final String JWT_TOKEN_HEADER = "Jwt-Token";

    public static final long EXPIRATION_TIME = 432_000_000;
}
