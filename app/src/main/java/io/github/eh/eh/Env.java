package io.github.eh.eh;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

public class Env {


    private static ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static final String API_URL = "http://10.0.2.2:8080/api/v1/user/login";
    public static final String AUTH_LOGIN_API_URL = "http://10.0.2.2:8080/api/v1/user/login";
    public static final String AUTH_CHK_API_URL = "http://10.0.2.2:8080/api/v1/auth/verify";
    public static final String AUTH_REGISTER_API_URL = "http://10.0.2.2:8080/api/v1/user/register";


    public static final String REQ_RESTAURANT_LIST_URL = "http://10.0.2.2:8080/api/v1/misc/restaurant";

    public static final String REQ_FRIEND_LIST_URL = "http://10.0.2.2:8080/api/v1/chat/friend";

    public static final String REQ_MATCHED_LIST_URL = "http://10.0.2.2:8080/api/v1/chat/matched";

    public static final String CHAT_POOL_URL = "10.0.2.2";

    public static final String MATCHING_POOL_URL = "10.0.2.2";
    public static final int PORT = 1300;
    public static final int CHAT_PORT = 1400;
    public static final int VERIFICATION_TIME_OUT = 110 * 1000;
    public static final int HTTP_PORT = 8080;

    public static class Bundle {

        public static final String BUNDLE_NAME = "passableResources";
        public static final String USER_BUNDLE = "user";
        public static final String CLASS_BUNDLE = "className";
        public static final String TARGET_USER_ID_BUNDLE = "chatInfo";
    }
}
