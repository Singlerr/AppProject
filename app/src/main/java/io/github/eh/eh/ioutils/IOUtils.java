package io.github.eh.eh.ioutils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1)
            buffer.write(data, 0, nRead);

        return buffer.toByteArray();
    }
    public static void transferTo(InputStream inputStream, ByteArrayOutputStream bos) throws IOException {
        byte[] buffer = new byte[4096];
        int len;
        while ((len = inputStream.read(buffer)) > -1){
            bos.write(buffer,0,len);
        }
        bos.flush();
    }
    public static String readText(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }
}
