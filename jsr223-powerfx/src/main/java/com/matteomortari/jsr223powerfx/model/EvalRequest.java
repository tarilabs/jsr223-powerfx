package com.matteomortari.jsr223powerfx.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvalRequest {
    @JsonProperty
    private String context;
    @JsonProperty
    private String expression;

    public String getContext() {
        return context;
    }
    public void setContext(String context) {
        this.context = context;
    }
    public String getExpression() {
        return expression;
    }
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
}
