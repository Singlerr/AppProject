package io.github.eh.eh.http;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.eh.eh.http.bundle.ResponseBundle;
import io.github.eh.eh.http.cipher.CipherBase;

public class HTTPBootstrap {

    public static int PORT;
    public static String HTTP_LOGIN = "";
    public static String HTTP_REGISTER = "";
    private final int timeOut = 5;
    private StreamHandler handler;
    private String host;
    private int port;

    private String token;

    public static Builder builder() {
        return new Builder();
    }

    public void submit() throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        URL url = new URL(host);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //con.setConnectTimeout(timeOut);
        con.setDoOutput(true);
        con.setReadTimeout(2000);
        con.addRequestProperty("Content-Type", "application/json");

        if (token != null && !token.isEmpty())
            con.setRequestProperty("Authorization", "Bearer " + token);

        OutputStream stream = con.getOutputStream();
        HTTPContext httpCtx = HTTPContext.getInstance(stream);

        handler.onWrite(httpCtx);

        stream.flush();
        stream.close();
        int resp = con.getResponseCode();
        InputStream inputStream = con.getInputStream();
        ObjectMapper mapper = new ObjectMapper();

        ResponseBundle responseBundle = mapper.readValue(CipherBase.getInstance().decode(inputStream), ResponseBundle.class);
        handler.onRead(responseBundle);
        //Free memory
        mapper = null;

        inputStream.close();
    }

    public static class Builder {
        private final HTTPBootstrap bootstrap;

        private Builder() {
            this.bootstrap = new HTTPBootstrap();
        }

        public Builder port(int port) {
            bootstrap.port = port;
            return this;
        }

        public Builder host(String host) {
            bootstrap.host = host;
            return this;
        }

        public Builder streamHandler(StreamHandler handler) {
            bootstrap.handler = handler;
            return this;
        }

        public Builder token(String token) {
            bootstrap.token = token;
            return this;
        }

        public HTTPBootstrap build() {
            return bootstrap;
        }
    }
}