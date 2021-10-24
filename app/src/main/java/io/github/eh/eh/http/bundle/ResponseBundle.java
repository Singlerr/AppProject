package io.github.eh.eh.http.bundle;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.github.eh.eh.Env;
import lombok.Builder;
import lombok.Getter;

@Builder
public class ResponseBundle {

    private int responseCode;
    private String response;

    public <T> T getMessage(Class<T> aClass) throws JsonProcessingException {
        return Env.getMapper().readValue(response,aClass);
    }


    public int getResponseCode() {
        return responseCode;
    }

    public String getResponse() {
        return response;
    }
}
