package io.github.eh.eh.http.bundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.eh.eh.Env;

import lombok.Builder;
import lombok.Getter;


public class RequestBundle {

    private String message;


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(Object o) throws JsonProcessingException {
        this.message = Env.getMapper().writeValueAsString(o);
    }
    public <T> T getMessage(Class<T> aClass) throws JsonProcessingException {
        return Env.getMapper().readValue(message,aClass);
    }
}
