package io.github.eh.eh.http;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.eh.eh.Env;
import io.github.eh.eh.http.cipher.CipherBase;
import io.github.eh.eh.ioutils.IOUtils;
import io.github.eh.eh.netty.utils.ObjectSerializer;

public class HTTPBootstrap {

    public static int PORT;
    public static String HTTP_LOGIN = "";
    public static String HTTP_REGISTER = "";
    private final int timeOut = 5;

    private String method = "POST";
    private StreamHandler handler;
    private String host;
    private int port;

    private String token;

    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";

    public static Builder builder() {
        return new Builder();
    }

    public void submit() throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, JSONException {
        URL url = new URL(host);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod(method);
        con.setReadTimeout(2000);
        con.addRequestProperty("Content-Type", "application/json");

        if (token != null && !token.isEmpty())
            con.setRequestProperty("Authorization", "Bearer " + token);

        OutputStream stream = con.getOutputStream();
        HTTPContext httpCtx = HTTPContext.getInstance(stream);

        handler.onWrite(httpCtx);

        stream.flush();
        stream.close();

        con.getResponseCode();
        InputStream inputStream = con.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.transferTo(CipherBase.getInstance().decode(inputStream),bos);
        byte[] data = bos.toByteArray();
        for(Class<?> cls : Env.REGISTERED_CLASSES){
            try{
                Object o = Env.getMapper().readValue(data,cls);
                handler.onRead(o);
            }catch (Exception ex){
                continue;
            }
        }

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

        public Builder method(String method){
            bootstrap.method = method;
            return this;
        }
        public HTTPBootstrap build() {
            return bootstrap;
        }
    }
}