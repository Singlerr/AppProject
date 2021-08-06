package io.github.eh.eh.http;

import org.jetbrains.annotations.NotNull;

public interface StreamHandler {

    void onWrite(@NotNull HTTPContext outputStream);

    void onRead(Object obj);

}
