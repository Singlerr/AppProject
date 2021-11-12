package io.github.eh.eh.http.bundle;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.github.eh.eh.Env;

@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
public class ResponseBundle {

    private int responseCode;
    private String response;


    public ResponseBundle() {
    }

    public ResponseBundle(String response, int responseCode) {
        this.response = response;
        this.responseCode = responseCode;
    }

    public <T> T getMessage(Class<T> aClass) throws JsonProcessingException {
        return Env.getMapper().readValue(response, aClass);
    }


    public int getResponseCode() {
        return responseCode;
    }

    public String getResponse() {
        return response;
    }

}
