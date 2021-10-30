package io.github.eh.eh.http.bundle;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

import io.github.eh.eh.Env;

public class RequestBundle {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(Object o) throws JsonProcessingException {
        this.message = Env.getMapper().writeValueAsString(o);
    }

    public <T> T getMessage(Class<T> aClass) throws IOException {
        return Env.getMapper().readValue(message, aClass);
    }
}
