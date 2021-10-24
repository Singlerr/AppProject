package io.github.eh.eh.http;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.eh.eh.Env;
import io.github.eh.eh.http.cipher.CipherBase;

public class HTTPContext {

    private final OutputStream outputStream;

    private HTTPContext(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public static HTTPContext getInstance(OutputStream outputStream) {
        return new HTTPContext(outputStream);
    }

    public void write(Object o) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        outputStream.write(CipherBase.getInstance().encode(Env.getMapper().writeValueAsBytes(o)));
    }

    public void write(byte[] data, boolean encrypted) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if (!encrypted)
            outputStream.write(data);
        else
            outputStream.write(CipherBase.getInstance().encode(data));
    }
}
