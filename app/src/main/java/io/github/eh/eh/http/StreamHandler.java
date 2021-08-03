package io.github.eh.eh.http;

public interface StreamHandler {

    void onWrite(HTTPContext outputStream);

    void onRead(Object obj);

}
