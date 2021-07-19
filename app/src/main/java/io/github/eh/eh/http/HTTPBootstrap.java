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

    private StreamHandler handler;

    public static int PORT;

    public static String HTTP_LOGIN = "";

    public static String HTTP_REGISTER = "";

    private String host;

    private int port;

    private int timeOut = 5;

    public void submit() throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        URL url = new URL(host);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //con.setConnectTimeout(timeOut);
        con.setDoOutput(true);
        OutputStream stream = con.getOutputStream();
        HTTPContext httpCtx = HTTPContext.getInstance(stream);

        handler.onWrite(httpCtx);

        stream.flush();
        stream.close();

        InputStream inputStream = con.getInputStream();

        ObjectMapper mapper = new ObjectMapper();

        ResponseBundle responseBundle = mapper.readValue(CipherBase.getInstance().decode(inputStream), ResponseBundle.class);
        handler.onRead(responseBundle);
        //Free memory
        mapper = null;

        inputStream.close();
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private HTTPBootstrap bootstrap;
        private Builder(){
            this.bootstrap = new HTTPBootstrap();
        }
        public Builder port(int port){
            bootstrap.port = port;
            return this;
        }
        public Builder host(String host){
            bootstrap.host = host;
            return this;
        }
        public Builder streamHandler(StreamHandler handler){
            bootstrap.handler = handler;
            return this;
        }
        public HTTPBootstrap build(){
            return bootstrap;
        }
    }
}