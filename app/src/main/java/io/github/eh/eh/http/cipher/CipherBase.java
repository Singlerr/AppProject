package io.github.eh.eh.http.cipher;

import android.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import io.github.eh.eh.ioutils.IOUtils;

public class CipherBase {
    private final String PRIVATE_KEY = "Vaf6vj6MmVo1NIUbKfk1SfXx3JGdM48B";

    private static CipherBase instance;

    private CipherBase(){}

    public static CipherBase getInstance(){
        if(instance == null)
            return (instance = new CipherBase());
        return instance;
    }
    public byte[] encode(InputStream inputStream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] data = IOUtils.readAllBytes(inputStream);

        SecretKey secretKey = new SecretKeySpec(data,"AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,new IvParameterSpec(PRIVATE_KEY.substring(0,16).getBytes(StandardCharsets.UTF_8)));

        byte[] encoded = Base64.decode(data,Base64.DEFAULT);

        return cipher.doFinal(encoded);
    }
    public byte[] encode(byte[] data) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKey secretKey = new SecretKeySpec(data,"AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,new IvParameterSpec(PRIVATE_KEY.substring(0,16).getBytes(StandardCharsets.UTF_8)));

        byte[] encoded = android.util.Base64.decode(data,Base64.DEFAULT);

        return cipher.doFinal(encoded);
    }

    public InputStream decode(InputStream inputStream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKey secretKey = new SecretKeySpec(IOUtils.readAllBytes(inputStream),"AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,secretKey,new IvParameterSpec(PRIVATE_KEY.substring(0,16).getBytes(StandardCharsets.UTF_8)));
        return new CipherInputStream(inputStream,cipher);
    }
}
