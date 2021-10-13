package io.github.eh.eh;

public class Env {
    public static final String API_URL = "http://10.0.2.2:8080/api/v1/user/login";
    public static final String AUTH_REQ_API_URL = "http://10.0.2.2:8080/api/v1/auth/msg";
    public static final String AUTH_CHK_API_URL = "http://10.0.2.2:8080/api/v1/auth/verify";

    public static final String REQ_RESTAURANT_LIST_URL = "http://10.0.2.2:8080/api/v1/auth/msg";

    public static final String MATCHING_POOL_URL = "10.0.2.2";
    public static final int PORT = 1300;
    public static final int VERIFICATION_TIME_OUT = 110 * 1000;
    public static final int HTTP_PORT = 8080;

    public static class Bundle {
        public static final String BUNDLE_NAME = "passableResources";
        public static final String USER_BUNDLE = "user";
        public static final String CLASS_BUNDLE = "className";
    }
}
