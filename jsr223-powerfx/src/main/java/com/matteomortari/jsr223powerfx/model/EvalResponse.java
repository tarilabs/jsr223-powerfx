package com.matteomortari.jsr223powerfx.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvalResponse {
    @JsonProperty
    private String result;
    @JsonProperty
    private String error;

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

}
