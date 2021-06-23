package io.github.eh.eh.http;

import io.github.eh.eh.http.bundle.ResponseBundle;

public interface StreamHandler {

    void onWrite(HTTPContext outputStream);
    void onRead(ResponseBundle bundle);

}
