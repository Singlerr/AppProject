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
import lombok.Builder;

@Builder
public class HTTPBootstrap {

    private StreamHandler handler;

    private String host = "localhost";

    private int port = 1300;

    private int timeOut = 5;

    public void submit() throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        URL url = new URL(host);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(timeOut);

        OutputStream stream = con.getOutputStream();
        HTTPContext httpCtx = HTTPContext.getInstance(stream);

        handler.onWrite(httpCtx);

        stream.flush();
        stream.close();

        InputStream inputStream = con.getInputStream();

        ObjectMapper mapper = new ObjectMapper();

        ResponseBundle responseBundle = mapper.readValue(CipherBase.getInstance().decode(inputStream), ResponseBundle.class);

        handler.onRead(responseBundle);

        inputStream.close();
    }
}
